package net.justapie.majutsu.db.schema.user;

import javafx.application.Platform;
import net.justapie.majutsu.db.repository.book.BookRepository;
import net.justapie.majutsu.db.repository.book.BookRepositoryFactory;
import net.justapie.majutsu.db.schema.book.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class User {
    protected long id;
    protected String name;
    protected String email;
    protected String hashedPassword;
    protected UserRole role;
    protected boolean active;
    protected List<Book> borrowedBooks;
    protected List<Book> availableBooks;
    protected Date createdAt;
    protected boolean firstLogin;
    
    // New fields for lazy loading
    protected String rawBorrowedBooks;
    protected boolean booksLoading = false;
    protected boolean booksLoaded = false;

    public static User fromResultSet(ResultSet resultSet) {
        final User user = new User();

        try {
            user.id = resultSet.getLong("id");
            user.name = resultSet.getString("name");
            user.email = resultSet.getString("email");
            user.hashedPassword = resultSet.getString("password");
            user.role = UserRole.valueOf(resultSet.getString("role"));
            user.active = resultSet.getBoolean("active");
            user.createdAt = Date.from(Instant.ofEpochSecond(resultSet.getLong("created_at")));
            user.firstLogin = resultSet.getBoolean("first_login");

            // Store raw borrowed books string for lazy loading
            user.rawBorrowedBooks = resultSet.getString("borrowed_books");
            if (user.rawBorrowedBooks == null) {
                user.rawBorrowedBooks = "";
            }

            // Initialize empty lists - will be populated lazily
            user.borrowedBooks = null;
            user.availableBooks = null;
            user.booksLoaded = false;

        } catch (SQLException e) {
            return null;
        }

        return user;
    }

    public static User fromLiteResultSet(ResultSet resultSet) {
        final User user = new User();

        try {
            user.id = resultSet.getLong("id");
            user.name = resultSet.getString("name");
            user.email = resultSet.getString("email");
            user.hashedPassword = resultSet.getString("password");
            user.role = UserRole.valueOf(resultSet.getString("role"));
            user.active = resultSet.getBoolean("active");
            user.createdAt = Date.from(Instant.ofEpochSecond(resultSet.getLong("created_at")));
            user.firstLogin = resultSet.getBoolean("first_login");

            // Store raw borrowed books string for lazy loading
            user.rawBorrowedBooks = resultSet.getString("borrowed_books");
            if (user.rawBorrowedBooks == null) {
                user.rawBorrowedBooks = "";
            }

            // Initialize empty lists - will be populated lazily
            user.borrowedBooks = null;
            user.availableBooks = null;
            user.booksLoaded = false;

        } catch (SQLException e) {
            return null;
        }

        return user;
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getHashedPassword() {
        return this.hashedPassword;
    }

    public UserRole getRole() {
        return this.role;
    }

    public List<Book> getBorrowedBooks() {
        if (!booksLoaded && !booksLoading) {
            loadBooksAsync();
        }
        return borrowedBooks != null ? borrowedBooks : new ArrayList<>();
    }

    public List<Book> getAvailableBooks() {
        if (!booksLoaded && !booksLoading) {
            loadBooksAsync();
        }
        return availableBooks != null ? availableBooks : new ArrayList<>();
    }

    private void loadBooksAsync() {
        if (booksLoading) return;
        
        booksLoading = true;
        
        CompletableFuture.runAsync(() -> {
            try {
                List<String> bookIds = Arrays.stream(rawBorrowedBooks.split(","))
                        .filter(id -> !id.trim().isEmpty())
                        .toList();

                // Use original bookIds for both calls - batchBookFetch no longer mutates the list
                List<Book> borrowed = BookRepositoryFactory.getInstance().create().batchBookFetch(bookIds);
                List<Book> available = BookRepositoryFactory.getInstance().create().fetchBooksExcept(bookIds);

                Platform.runLater(() -> {
                    this.borrowedBooks = borrowed;
                    this.availableBooks = available;
                    this.booksLoaded = true;
                    this.booksLoading = false;
                });
            } catch (Exception e) {
                Platform.runLater(() -> {
                    this.borrowedBooks = new ArrayList<>();
                    this.availableBooks = new ArrayList<>();
                    this.booksLoaded = true;
                    this.booksLoading = false;
                });
            }
        });
    }

    public boolean areBooksLoaded() {
        return booksLoaded;
    }

    public boolean areBooksLoading() {
        return booksLoading;
    }

    public boolean isActive() {
        return this.active;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public boolean isFirstLogin() {
        return this.firstLogin;
    }
}
