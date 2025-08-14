package net.justapie.majutsu.db.schema.user;

import net.justapie.majutsu.db.repository.book.BookRepositoryFactory;
import net.justapie.majutsu.db.schema.book.Book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class User {
    protected long id;
    protected String name;
    protected String email;
    protected String hashedPassword;
    protected UserRole role;
    protected boolean active;
    protected List<Book> borrowedBooks;
    protected Date createdAt;

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

            String rawBorrowedBooks = resultSet.getString("borrowed_books");
            List<String> bookIds = Arrays.stream(rawBorrowedBooks.split(",")).toList();

            user.borrowedBooks = BookRepositoryFactory.getInstance().create().batchBookFetch(bookIds);

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
        return this.borrowedBooks;
    }

    public boolean isActive() {
        return this.active;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }
}
