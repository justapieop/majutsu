package net.justapie.majutsu.gui.controller;

import javafx.application.Platform;
import net.justapie.majutsu.db.repository.document.DocumentRepositoryFactory;
import net.justapie.majutsu.gui.SceneType;

public class DashboardSplashController extends BaseSplashController {
    public DashboardSplashController() {
        super(SceneType.DASHBOARD);
    }

    @Override
    public void process() {
        new Thread(() -> {
            DocumentRepositoryFactory.getInstance().create().getAllBooks();

            Platform.runLater(() -> this.switchToScene(this.getNextScene()));
        }).start();
    }
}
