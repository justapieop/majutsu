package me.justapie.majutsu.db.schema;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private long id;
    private String name;
    private String email;
    private String hashedPassword;
    private UserRole role;

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
        } catch (SQLException e) {
            return null;
        }

        return user;
    }
}
