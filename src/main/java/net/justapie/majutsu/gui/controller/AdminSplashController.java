package net.justapie.majutsu.gui.controller;

import net.justapie.majutsu.db.repository.book.BookRepositoryFactory;
import net.justapie.majutsu.gui.SceneType;

public class AdminSplashController extends BaseSplashController {
    public AdminSplashController() {
        super(SceneType.ADMIN);
    }

    @Override
    public void process() {
        new Thread(() -> {
            BookRepositoryFactory.getInstance().create().getAllBooks();

            this.nextScene();
        }).start();
    }
}
