package net.justapie.majutsu.db.schema.user;

import net.justapie.majutsu.db.DbClient;
import net.justapie.majutsu.db.repository.book.BookRepositoryFactory;
import net.justapie.majutsu.db.schema.book.Book;
import net.justapie.majutsu.utils.CryptoUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User {
    private long id;
    private String name;
    private String email;
    private String hashedPassword;
    private UserRole role;
    private boolean active;
    private List<Book> borrowedBooks;

    public static User fromResultSet(ResultSet resultSet) {
        final User user = new User();

        try {
            user.id = resultSet.getLong("id");
            user.name = resultSet.getString("name");
            user.email = resultSet.getString("email");
            user.hashedPassword = resultSet.getString("password");
            user.role = UserRole.valueOf(resultSet.getString("role"));
            user.active = resultSet.getBoolean("active");

            String rawBorrowedBooks = resultSet.getString("borrowed_books");
            List<String> bookIds = Arrays.stream(rawBorrowedBooks.split(",")).toList();
            List<Book> books = new ArrayList<>();

            for (final String id : bookIds) {
                Book book = BookRepositoryFactory.getInstance().create().getBookById(id);

                books.add(book);
            }

            user.borrowedBooks = books;

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

    public void changePassword(String currentPassword, String newPassword) {
        if (!this.verifyPassword(currentPassword)) {
            return;
        }
        String newHashedPassword = CryptoUtils.getInstance().hashPassword(newPassword);
        Connection connection = DbClient.getInstance().getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET password = ? WHERE id = ?"
            );

            statement.setString(1, newHashedPassword);
            statement.setLong(2, this.id);

            statement.executeUpdate();
        } catch (SQLException ignored) {
        }

        this.hashedPassword = newHashedPassword;
    }

    public void changeEmail(String current, String email) {
        if (!this.verifyPassword(current)) {
            return;
        }
        Connection connection = DbClient.getInstance().getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET email = ? WHERE id = ?"
            );

            statement.setString(1, email);
            statement.setLong(2, this.id);

            statement.executeUpdate();
        } catch (SQLException ignored) {
        }

        this.email = email;
    }

    public void changeName(String name) {
        Connection connection = DbClient.getInstance().getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET name = ? WHERE id = ?"
            );

            statement.setString(1, name);
            statement.setLong(2, this.id);

            statement.executeUpdate();
        } catch (SQLException ignored) {
        }

        this.name = name;
    }

    private boolean verifyPassword(String password) {
        return CryptoUtils.getInstance().comparePassword(password, this.hashedPassword);
    }
}
