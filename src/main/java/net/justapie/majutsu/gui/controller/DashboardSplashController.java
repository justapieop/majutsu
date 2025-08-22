package net.justapie.majutsu.gui.controller;

import javafx.application.Platform;
import net.justapie.majutsu.cache.Cache;
import net.justapie.majutsu.db.repository.book.BookRepositoryFactory;
import net.justapie.majutsu.gui.SceneType;

public class DashboardSplashController extends BaseSplashController {
    public DashboardSplashController() {
        super(SceneType.DASHBOARD);
    }

    @Override
    public void process() {
        new Thread(() -> {
            // Force refresh by invalidating all user and book caches
            // This ensures dashboard shows fresh data after admin operations
            Cache.getInstance().removeByPrefix("user:");
            Cache.getInstance().removeByPrefix("book:");
            Cache.getInstance().remove("books");
            
            // Fetch fresh book data
            BookRepositoryFactory.getInstance().create().getAllBooks();
            this.nextScene();
        }).start();
    }
}
