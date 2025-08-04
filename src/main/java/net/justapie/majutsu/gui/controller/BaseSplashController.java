package net.justapie.majutsu.gui.controller;

import javafx.application.Platform;
import net.justapie.majutsu.gui.SceneType;

public abstract class BaseSplashController extends BaseController {
    private final String nextScene;

    protected BaseSplashController(String nextScene) {
        this.nextScene = nextScene;
        Platform.runLater(() -> this.switchToScene(SceneType.SPLASH));
    }

    public String getNextScene() {
        return this.nextScene;
    }

    public abstract void process();
}
