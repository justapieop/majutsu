package net.justapie.majutsu.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneManager {
    public static Scene loadScene(String path) {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(path));

        try {
            return new Scene(loader.load());
        } catch (IOException e) {
            return null;
        }
    }

    public static void triggerSubWindow(Scene scene) {
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }
}
