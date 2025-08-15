package net.justapie.majutsu.gui.controller;

import ch.qos.logback.classic.Logger;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.justapie.majutsu.gui.SceneManager;
import net.justapie.majutsu.gui.UI;
import net.justapie.majutsu.utils.Utils;

public abstract class BaseController {
    private static final Logger LOGGER = Utils.getInstance().getRootLogger();

    public void switchToScene(String path) {
        LOGGER.debug("Loading scene {}", path);
        Stage stage = UI.stage;

        Scene newScene = SceneManager.loadScene(path);

        stage.setScene(newScene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.sizeToScene();
    }
}
