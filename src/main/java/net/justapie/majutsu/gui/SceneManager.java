package net.justapie.majutsu.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

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

    public static FXMLLoader getLoader(String path) {
        return new FXMLLoader(SceneManager.class.getResource(path));
    }
}
