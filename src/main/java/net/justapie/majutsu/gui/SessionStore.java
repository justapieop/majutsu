package net.justapie.majutsu.gui;

import net.justapie.majutsu.cache.Cache;
import net.justapie.majutsu.cache.CacheObject;
import net.justapie.majutsu.db.repository.user.UserRepositoryFactory;
import net.justapie.majutsu.db.schema.user.User;

import java.util.Objects;

public class SessionStore {
    private static final SessionStore INSTANCE = new SessionStore();

    private long currentUserId;

    public SessionStore() {
    }

    public static SessionStore getInstance() {
        return INSTANCE;
    }

    public void setCurrentUserId(long userId) {
        this.currentUserId = userId;
    }

    public User getCurrentUser() {
        return UserRepositoryFactory.getInstance().create().getUserById(this.currentUserId);
    }

    public void clearSession() {
        this.currentUserId = 0;
    }

    public boolean isLoggedIn() {
        return this.currentUserId != 0;
    }
}
