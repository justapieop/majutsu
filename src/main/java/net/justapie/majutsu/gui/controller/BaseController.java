package net.justapie.majutsu.gui.controller;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.justapie.majutsu.gui.SceneManager;
import net.justapie.majutsu.gui.UI;

public abstract class BaseController {
    public void closeScene(Node node) {
        Stage stage = (Stage) node.getScene().getWindow();

        stage.close();
    }

    public void switchToScene(String path) {
        Stage stage = UI.stage;

        Scene newScene = SceneManager.loadScene(path);

        stage.setScene(newScene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.sizeToScene();
    }
}
