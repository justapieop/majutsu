package net.justapie.majutsu.gui.controller;

import javafx.application.Platform;
import net.justapie.majutsu.db.repository.document.DocumentRepositoryFactory;
import net.justapie.majutsu.gui.SceneType;

public class AdminSplashController extends BaseSplashController {
    public AdminSplashController() {
        super(SceneType.ADMIN);
    }

    @Override
    public void process() {
        new Thread(() -> {
            DocumentRepositoryFactory.getInstance().create().getAllBooks();

            Platform.runLater(() -> this.switchToScene(this.getNextScene()));
        }).start();
    }
}
