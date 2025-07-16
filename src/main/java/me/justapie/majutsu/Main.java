package me.justapie.majutsu;

import ch.qos.logback.classic.Logger;
import me.justapie.majutsu.db.DbClient;
import me.justapie.majutsu.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Main {
    private static final Logger LOGGER = Utils.getInstance().getRootLogger().getLoggerContext().getLogger(Main.class);
    public static void main(String[] args) {
        LOGGER.debug("Debug mode enabled. You will see more logs");
        LOGGER.info("Initializing application");

        LOGGER.debug("Checking data.db availability");
        File dbFile = new File("data.db");

        if (!dbFile.exists()) {
            LOGGER.debug("Database file not exists. Trying to create a new database");
            try {
                boolean success = dbFile.createNewFile();
                if (!success) {
                    LOGGER.error("Database file cannot be created. Exiting application");
                    System.exit(1);
                } else {
                    LOGGER.debug("Database file created");
                }
            } catch (IOException e) {
                LOGGER.error("Failed to create the database file. Exiting application");
                LOGGER.error(e.getMessage());
                System.exit(1);
            }
        }

        LOGGER.info("Connecting to database");
        try {
            DbClient.getInstance().connect();
            LOGGER.info("Database connected");
        } catch (SQLException e) {
            LOGGER.error("Failed to connect to database. Exiting application");
            LOGGER.error(e.getMessage());
            System.exit(1);
        }

        LOGGER.info("Launching windows");
    }
}
