package net.justapie.majutsu.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.justapie.majutsu.gui.controller.Selectable;

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

    public static void triggerSubWindow(Scene scene, Selectable subWindowInterface) {
        Stage stage = new Stage();
        subWindowInterface.setStage(stage);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.sizeToScene();
        stage.setResizable(false);
        stage.show();
    }
}
