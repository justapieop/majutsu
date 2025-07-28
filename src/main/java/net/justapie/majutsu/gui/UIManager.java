package net.justapie.majutsu.gui;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.stage.Stage;

public class UIManager extends Application {
    @Override
    public void start(Stage primaryStage) {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        primaryStage.setTitle("Welcome to Majutsu - Library Management Tool.");
        primaryStage.setScene(
                SceneManager.loadScene(SceneType.LOGIN)
        );
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}
