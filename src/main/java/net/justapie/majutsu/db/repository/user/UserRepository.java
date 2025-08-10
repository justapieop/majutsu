package net.justapie.majutsu.db.repository.user;

import ch.qos.logback.classic.Logger;
import net.justapie.majutsu.db.DbClient;
import net.justapie.majutsu.db.repository.book.BookRepositoryFactory;
import net.justapie.majutsu.db.repository.history.HistoryRepositoryFactory;
import net.justapie.majutsu.db.schema.book.Book;
import net.justapie.majutsu.db.schema.user.User;
import net.justapie.majutsu.db.schema.user.UserRole;
import net.justapie.majutsu.gbook.model.Volume;
import net.justapie.majutsu.utils.CryptoUtils;
import net.justapie.majutsu.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserRepository {
    private static final Logger LOGGER = Utils.getInstance().getRootLogger().getLoggerContext().getLogger(UserRepository.class);
    private static final Connection CONNECTION = DbClient.getInstance().getConnection();

    UserRepository() {
    }

    public Book borrowBook(long userId, String bookId) {
        LOGGER.debug("Borrowing book {} for user {}", bookId, userId);
        User user = this.getUserById(userId);
        if (Objects.isNull(user)) {
            LOGGER.debug("Nonexistent user");
            return null;
        }

        Book book = BookRepositoryFactory.getInstance().create().getBookById(bookId);

        if (Objects.isNull(book)) {
            LOGGER.debug("Nonexistent book");
            return null;
        }

        if (user.getBorrowedBooks().contains(book)) {
            LOGGER.debug("User {} already borrowed book {}", user, bookId);
            return null;
        }

        user.getBorrowedBooks().add(book);

        String newBookIdStr = user.getBorrowedBooks().stream().map(
                Volume::getId
        ).collect(Collectors.joining(","));

        try (PreparedStatement stmt = CONNECTION.prepareStatement(
                "UPDATE users SET borrowed_books = ? WHERE id = ?"
        )) {
            stmt.setString(1, newBookIdStr);
            stmt.setLong(2, userId);
            stmt.executeUpdate();

            HistoryRepositoryFactory.getInstance().create().recordBorrow(userId, bookId);

            return book;
        } catch (SQLException e) {
            LOGGER.error("Failed while borrowing book {} for user {}", bookId, userId);
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public void returnBook(long userId, String bookId) {
        LOGGER.debug("Returning book {} for user {}", bookId, userId);
        User user = this.getUserById(userId);
        if (Objects.isNull(user)) {
            LOGGER.debug("Nonexistent user");
            return;
        }

        if (!user.getBorrowedBooks().stream().map(Book::getId).toList().contains(bookId)) {
            LOGGER.debug("User {} did not borrowed book {}", user, bookId);
            return;
        }

        user.getBorrowedBooks().removeIf(b -> b.getId().equals(bookId));

        String newBookIdStr = user.getBorrowedBooks().stream().map(
                Volume::getId
        ).collect(Collectors.joining(","));

        try (PreparedStatement stmt = CONNECTION.prepareStatement(
                "UPDATE users SET borrowed_books = ? WHERE id = ?"
        )) {
            stmt.setString(1, newBookIdStr);
            stmt.setLong(2, userId);
            stmt.executeUpdate();

            HistoryRepositoryFactory.getInstance().create().recordReturn(userId, bookId);
        } catch (SQLException e) {
            LOGGER.error("Failed while returning book {} for user {}", bookId, userId);
            LOGGER.error(e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();
        LOGGER.debug("Preparing to fetch all users");

        try {
            PreparedStatement statement = CONNECTION.prepareStatement(
                    "SELECT * FROM users;"
            );

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                users.add(User.fromResultSet(result));
            }
        } catch (SQLException e) {
            return users;
        }

        return Collections.unmodifiableList(users);
    }

    public String getPassword(String email) {
        ResultSet result;
        try {
            PreparedStatement statement = CONNECTION.prepareStatement("SELECT password FROM users WHERE email = ?");
            statement.setString(1, email.toLowerCase());

            result = statement.executeQuery();

            return result.getString("password");
        } catch (SQLException e) {
            LOGGER.error("Error occurred while getting user password");
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public long getUserIdByEmail(String email) {
        ResultSet result;
        LOGGER.debug("Preparing to fetch user id by email");
        try {
            PreparedStatement statement = CONNECTION.prepareStatement("SELECT * FROM users WHERE email = ?");
            statement.setString(1, email);

            result = statement.executeQuery();
            return result.getLong("id");
        } catch (SQLException e) {
            LOGGER.error("Error occurred while getting user id by email");
            LOGGER.error(e.getMessage());
            return 0;
        }
    }

    public User getUserById(long id) {
        ResultSet result;
        LOGGER.debug("Preparing to fetch user by id");
        try {
            PreparedStatement statement = CONNECTION.prepareStatement("SELECT * FROM users WHERE id = ?");
            statement.setLong(1, id);

            result = statement.executeQuery();
        } catch (SQLException e) {
            LOGGER.error("Error occurred while getting user by id");
            LOGGER.error(e.getMessage());
            return null;
        }

        return User.fromResultSet(result);
    }

    public User getUserByEmail(String email) {
        LOGGER.debug("Preparing to fetch user by email");
        ResultSet result;
        try {
            PreparedStatement statement = CONNECTION.prepareStatement("SELECT * FROM users WHERE email = ?");
            statement.setString(1, email.toLowerCase());

            result = statement.executeQuery();
        } catch (SQLException e) {
            LOGGER.error("Error occurred while getting user by email");
            LOGGER.error(e.getMessage());
            return null;
        }

        return User.fromResultSet(result);
    }

    public void changePassword(long id, String password) {
        LOGGER.debug("Preparing to fetch change user {}'s password", id);
        try {
            PreparedStatement statement = CONNECTION.prepareStatement("UPDATE users SET password = ? WHERE id = ?");
            statement.setString(1, CryptoUtils.getInstance().hashPassword(password));
            statement.setLong(2, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error occurred while getting updating user password");
            LOGGER.error(e.getMessage());
        }
    }

    public void changeName(long id, String name) {
        LOGGER.debug("Preparing to change user {}'s name", id);
        try {
            PreparedStatement statement = CONNECTION.prepareStatement("UPDATE users SET name = ? WHERE id = ?");
            statement.setString(1, name);
            statement.setLong(2, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error occurred while getting updating user name");
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * Due to driver limitation, there must be two queries to be executed
     */
    public User createUser(String name, String email, String password) {
        LOGGER.debug("Preparing to create user");
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(
                            "INSERT INTO users (id, name, email, password, role) VALUES (?, ?, ?, ?, ?);"
            );

            statement.setLong(1, Utils.getInstance().generateSnowflakeId());
            statement.setString(2, name);
            statement.setString(3, email.toLowerCase());
            statement.setString(4, CryptoUtils.getInstance().hashPassword(password));
            statement.setString(5, UserRole.USER.toString());

            statement.executeUpdate();

            return this.getUserByEmail(email);
        } catch (SQLException e) {
            if (e.getMessage().startsWith("[SQLITE_CONSTRAINT_UNIQUE]")) return null;
            LOGGER.error("Error occurred while creating user");
            LOGGER.error(e.getMessage());
            return null;
        }
    }
}
