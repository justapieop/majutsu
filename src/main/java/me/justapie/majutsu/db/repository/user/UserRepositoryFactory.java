package me.justapie.majutsu.db.repository.user;

import ch.qos.logback.classic.Logger;
import me.justapie.majutsu.db.DbClient;
import me.justapie.majutsu.provider.RepositoryFactoryProvider;
import me.justapie.majutsu.utils.Utils;

import java.sql.Connection;
import java.sql.SQLException;

public class UserRepositoryFactory extends RepositoryFactoryProvider<UserRepository> {
    private static final Logger LOGGER = Utils.getInstance().getRootLogger().getLoggerContext().getLogger(UserRepositoryFactory.class);
    private static final UserRepositoryFactory INSTANCE = new UserRepositoryFactory();

    public UserRepository create() {
        final Connection connection = DbClient.getInstance().getConnection();

        try {
            LOGGER.info("Getting user repository. Creating repository if not exists");
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY UNIQUE NOT NULL," +
                    "name TEXT NOT NULL DEFAULT 'Unknown'," +
                    "password TEXT NOT NULL," +
                    "email TEXT UNIQUE NOT NULL," +
                    "role TEXT NOT NULL CHECK (role IN ('ADMIN', 'USER')) DEFAULT 'USER'," +
                    "created_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now'))," +
                    "active BOOLEAN NOT NULL DEFAULT true" +
                    ");");
            LOGGER.info("User repository created");

            LOGGER.info("Creating index for user");
            connection.createStatement().execute("CREATE UNIQUE INDEX IF NOT EXISTS user_idx ON users (" +
                    "id, email" +
                    ");");
            LOGGER.info("Index created");
        } catch (SQLException e) {
            LOGGER.error("Failed while getting user repository");
            LOGGER.error(e.getMessage());
        }

        if (this.repository == null)
            this.repository = new UserRepository();

        return this.repository;
    }

    public static UserRepositoryFactory getInstance() {
        return INSTANCE;
    }
}
