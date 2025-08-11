package net.justapie.majutsu.db.schema.user;

import net.justapie.majutsu.db.DbClient;
import net.justapie.majutsu.utils.CryptoUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    protected long id;
    protected String name;
    protected String email;
    protected String hashedPassword;
    protected UserRole role;
    protected boolean active;

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

    public static User fromResultSet(ResultSet resultSet) {
        final User user = new User();

        try {
            user.id = resultSet.getLong("id");
            user.name = resultSet.getString("name");
            user.email = resultSet.getString("email");
            user.hashedPassword = resultSet.getString("password");
            user.role = UserRole.valueOf(resultSet.getString("role"));
            user.active = resultSet.getBoolean("active");
        } catch (SQLException e) {
            return null;
        }

        return user;
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
                    "UPDATE users SET password = ?"
            );

            statement.setString(1, newHashedPassword);

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
                    "UPDATE users SET email = ?"
            );

            statement.setString(1, email);

            statement.executeUpdate();
        } catch (SQLException ignored) {
        }

        this.email = email;
    }

    public void changeName(String name) {
        Connection connection = DbClient.getInstance().getConnection();

        try {
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE users SET name = ?"
            );

            statement.setString(1, name);

            statement.executeUpdate();
        } catch (SQLException ignored) {
        }

        this.name = name;
    }

    private boolean verifyPassword(String password) {
        return CryptoUtils.getInstance().comparePassword(password, this.hashedPassword);
    }
}
