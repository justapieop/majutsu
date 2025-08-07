package net.justapie.majutsu.gui;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class UI extends Application {
    public static Stage stage;

    @Override
    public void start(Stage primaryStage) {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        stage = primaryStage;

        primaryStage.setTitle("Welcome to Majutsu - Library Management Tool.");
        primaryStage.setScene(
                SceneManager.loadScene(SceneType.LOGIN)
        );
        primaryStage.centerOnScreen();
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(event -> System.exit(0));
        primaryStage.show();
    }
}
