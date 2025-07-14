package me.justapie.majutsu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ch.qos.logback.classic.Logger;
import me.justapie.majutsu.db.DbClient;
import me.justapie.majutsu.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

public class Main extends Application {
    private static final Logger LOGGER = Utils.getInstance().getRootLogger().getLoggerContext().getLogger(Main.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1366, 768);
        primaryStage.setTitle("Hello!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

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
                LOGGER.error(Arrays.toString(e.getStackTrace()));
                System.exit(1);
            }
        }

        LOGGER.info("Connecting to database");
        try {
            DbClient.getInstance().connect();
            LOGGER.info("Database connected");
        } catch (SQLException e) {
            LOGGER.error("Failed to connect to database. Exiting application");
            LOGGER.error(Arrays.toString(e.getStackTrace()));
            System.exit(1);
        }

        LOGGER.info("Launching windows");
        launch();
    }
}
