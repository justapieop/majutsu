package net.justapie.majutsu.db.repository.user;

import ch.qos.logback.classic.Logger;
import net.justapie.majutsu.db.DbClient;
import net.justapie.majutsu.db.schema.User;
import net.justapie.majutsu.db.schema.UserRole;
import net.justapie.majutsu.utils.CryptoUtils;
import net.justapie.majutsu.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserRepository {
    private static final Logger LOGGER = Utils.getInstance().getRootLogger().getLoggerContext().getLogger(UserRepository.class);
    private static final Connection CONNECTION = DbClient.getInstance().getConnection();
    public UserRepository() {
        super();
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> users = new ArrayList<>();

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

        return users;
    }

    public String getPassword(String email) {
        ResultSet result;
        try {
            PreparedStatement statement = CONNECTION.prepareStatement("SELECT password FROM users WHERE email = ?");
            statement.setString(1, email);

            result = statement.executeQuery();

            return result.getString("password");
        } catch (SQLException e) {
            LOGGER.error("Error occurred while getting user password");
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public User getUserById(long id) {
        ResultSet result;
        try {
            PreparedStatement statement = CONNECTION.prepareStatement("SELECT * FROM users WHERE id = ?");
            statement.setLong(1, id);

            result = statement.executeQuery();
        } catch (SQLException e) {
            LOGGER.error("Error occurred while getting user");
            LOGGER.error(e.getMessage());
            return null;
        }

        return User.fromResultSet(result);
    }

    public User createUser(String name, String email, String password) {
        try {
            PreparedStatement statement = CONNECTION.prepareStatement(
                            "INSERT INTO users (id, name, email, password, role) VALUES (?, ?, ?, ?, ?);"
            );

            statement.setLong(1, Utils.getInstance().generateSnowflakeId());
            statement.setString(2, name);
            statement.setString(3, email);
            statement.setString(4, CryptoUtils.getInstance().hashPassword(password));
            statement.setString(5, UserRole.USER.toString());

            statement.executeUpdate();

            // Due to driver limitation, we have to query for the user
            long id = statement.getGeneratedKeys().getLong("id");
            return this.getUserById(id);
        } catch (SQLException e) {
            LOGGER.error("Error occurred while creating user");
            LOGGER.error(e.getMessage());
            return null;
        }
    }
}
