package net.justapie.majutsu.db.repository.user;

import ch.qos.logback.classic.Logger;
import net.justapie.majutsu.cache.Cache;
import net.justapie.majutsu.cache.CacheObject;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class UserRepository {
    private static final Logger LOGGER = Utils.getInstance().getRootLogger().getLoggerContext().getLogger(UserRepository.class);
    private static final Connection CONNECTION = DbClient.getInstance().getConnection();

    UserRepository() {
    }

    public boolean emailExists(String email) {
        LOGGER.debug("Checking if {} is already in db", email);

        try (PreparedStatement stmt = CONNECTION.prepareStatement(
                "SELECT EXISTS(SELECT 1 FROM users WHERE email = ?) AS exist;"
        )) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            boolean result = rs.getBoolean("exist");
            rs.close();
            return result;
        } catch (SQLException e) {
            LOGGER.error("Failed to check email {} existence", email);
            LOGGER.error(e.getMessage());
        }
        return false;
    }

    public void setFirstLogin(long userId, boolean firstLogin) {
        LOGGER.debug("Preparing to set user {} first login mode to {}", userId, firstLogin);

        try (PreparedStatement stmt = CONNECTION.prepareStatement(
                "UPDATE users SET first_login = ? WHERE id = ?"
        )) {
            stmt.setBoolean(1, firstLogin);
            stmt.setLong(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Failed to set user {} first login mode to {}", userId, firstLogin);
            LOGGER.error(e.getMessage());
        }
    }

    public void setActive(boolean active, List<Long> ids) {
        LOGGER.debug("Setting users {} activation mode to {}", ids, active);

        try (PreparedStatement stmt = CONNECTION.prepareStatement(
                "UPDATE users SET active = ? WHERE id = ?"
        )) {
            for (final Long id : ids) {
                stmt.setBoolean(1, active);
                stmt.setLong(2, id);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            LOGGER.error("Failed to set users {} activation mode to {}", ids, active);
            LOGGER.error(e.getMessage());
        }
    }

    public void deleteUser(List<Long> ids) {
        LOGGER.debug("Deleting users {}", ids);

        try (PreparedStatement stmt = CONNECTION.prepareStatement(
                "DELETE FROM users WHERE id = ?"
        )) {
            for (final Long id : ids) {
                stmt.setLong(1, id);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            LOGGER.error("Failed to delete users {}", ids);
            LOGGER.error(e.getMessage());
        }
    }

    public void borrowBook(long userId, String bookId) {
        LOGGER.debug("Borrowing book {} for user {}", bookId, userId);
        User user = this.getUserById(userId);
        if (Objects.isNull(user)) {
            LOGGER.debug("Nonexistent user of {} borrowing book", userId);
            return;
        }

        List<Book> modifiableBookList = new ArrayList<>(user.getBorrowedBooks());

        Book book = BookRepositoryFactory.getInstance().create().getBookById(bookId);

        if (Objects.isNull(book)) {
            LOGGER.debug("Nonexistent book {}", bookId);
            return;
        }

        if (
                user.getBorrowedBooks()
                        .stream()
                        .map(Volume::getId)
                        .toList()
                        .contains(bookId)
        ) {
            LOGGER.debug("User {} already borrowed book {}", userId, bookId);
            return;
        }

        LOGGER.debug("Previous number of book user {} borrowed: {}", userId, modifiableBookList.size());

        modifiableBookList.add(book);

        LOGGER.debug("Final number of book user {} borrowed: {}", userId, modifiableBookList.size());

        String newBookIdStr = modifiableBookList.stream().map(
                Volume::getId
        ).collect(Collectors.joining(","));

        LOGGER.debug("New borrowed bookId string {}", newBookIdStr);

        try (PreparedStatement stmt = CONNECTION.prepareStatement(
                "UPDATE users SET borrowed_books = ? WHERE id = ?"
        )) {
            stmt.setString(1, newBookIdStr);
            stmt.setLong(2, userId);
            stmt.executeUpdate();

            HistoryRepositoryFactory.getInstance().create().recordBorrow(userId, bookId);

        } catch (SQLException e) {
            LOGGER.error("Failed while borrowing book {} for user {}", bookId, userId);
            LOGGER.error(e.getMessage());
        }
    }

    public void returnBook(long userId, String bookId) {
        LOGGER.debug("Returning book {} for user {}", bookId, userId);
        User user = this.getUserById(userId);
        if (Objects.isNull(user)) {
            LOGGER.debug("Nonexistent user of id {} returning book", userId);
            return;
        }

        List<Book> modifiableBookList = new ArrayList<>(user.getBorrowedBooks());

        if (!modifiableBookList.stream().map(Book::getId).toList().contains(bookId)) {
            LOGGER.debug("User {} did not borrowed book {}", user, bookId);
            return;
        }

        modifiableBookList.removeIf(b -> b.getId().equals(bookId));

        String newBookIdStr = modifiableBookList.stream().map(
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
        LOGGER.debug("Checking cached users");

        CacheObject cachedUsers = Cache.getInstance().get("users");

        if (
                !Objects.isNull(cachedUsers)
                && !cachedUsers.isExpired()
                && cachedUsers.getData() instanceof List<?>
        ) {
            LOGGER.debug("Cached users hit");
            return (List<User>) cachedUsers.getData();
        }

        LOGGER.debug("Cached users miss. Fetching");

        try {
            PreparedStatement statement = CONNECTION.prepareStatement(
                    "SELECT * FROM users;"
            );

            ResultSet result = statement.executeQuery();

            while (result.next()) {
                users.add(User.fromResultSet(result));
            }

            result.close();
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
            String password = result.getString("password");
            result.close();
            return password;
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
            long userId = result.getLong("id");
            result.close();
            return userId;
        } catch (SQLException e) {
            LOGGER.error("Error occurred while getting user id by email");
            LOGGER.error(e.getMessage());
            return 0;
        }
    }

    public User getUserById(long id) {
        ResultSet result;
        LOGGER.debug("Preparing to fetch user of id {}", id);
        LOGGER.debug("Checking cache for user of id {}", id);
        CacheObject cachedUser = Cache.getInstance().get("user:" + id);

        if (!Objects.isNull(cachedUser) && !cachedUser.isExpired()) {
            LOGGER.debug("Cached user of id {} hit", id);
            return (User) cachedUser.getData();
        }

        LOGGER.debug("Cached user of id {} miss. Fetching", id);

        try {
            PreparedStatement statement = CONNECTION.prepareStatement("SELECT * FROM users WHERE id = ?");
            statement.setLong(1, id);

            result = statement.executeQuery();
            User user = User.fromResultSet(result);
            result.close();
            Cache.getInstance().put("user:" + id, user, Cache.createTtl(TimeUnit.HOURS, 1));
            return user;
        } catch (SQLException e) {
            LOGGER.error("Error occurred while getting user by id");
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public User getUserByEmail(String email) {
        LOGGER.debug("Preparing to fetch user of email {}", email);
        LOGGER.debug("Checking cached user {}", email);

        CacheObject cachedUser = Cache.getInstance().get("user:" + email);

        if (!Objects.isNull(cachedUser) && !cachedUser.isExpired()) {
            LOGGER.debug("Cached user of email {} hit", email);
            return (User) cachedUser.getData();
        }

        LOGGER.debug("Cached user of email {} miss. Fetching", email);

        ResultSet result;
        try {
            PreparedStatement statement = CONNECTION.prepareStatement("SELECT * FROM users WHERE email = ?");
            statement.setString(1, email.toLowerCase());

            result = statement.executeQuery();
            User user = User.fromResultSet(result);
            result.close();
            Cache.getInstance().put("user:" + email, user, Cache.createTtl(TimeUnit.HOURS, 1));
            return user;
        } catch (SQLException e) {
            LOGGER.error("Error occurred while getting user by email");
            LOGGER.error(e.getMessage());
            return null;
        }
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
    public User createUser(String name, String email, String password, boolean firstLogin) {
        LOGGER.debug("Preparing to create user");
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(
                            "INSERT INTO users (id, name, email, password, role, first_login, borrowed_books) VALUES (?, ?, ?, ?, ?, ?, ?);"
            );

            statement.setLong(1, Utils.getInstance().generateSnowflakeId());
            statement.setString(2, name);
            statement.setString(3, email.toLowerCase());
            statement.setString(4, CryptoUtils.getInstance().hashPassword(password));
            statement.setString(5, UserRole.USER.toString());
            statement.setBoolean(6, firstLogin);
            statement.setString(7, "");

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
