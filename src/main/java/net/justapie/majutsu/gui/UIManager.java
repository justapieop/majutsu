package net.justapie.majutsu.gui;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.stage.Stage;
import net.justapie.majutsu.gui.layouts.Login;

public class UIManager extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());

        Login loginScene = new Login();
        CompositionManager.getInstance().setScene(loginScene);

        primaryStage.setTitle("Welcome to Majutsu - Library Management Tool.");
        primaryStage.setScene(CompositionManager.getInstance().getCurrentScene());
        primaryStage.show();
    }
}
