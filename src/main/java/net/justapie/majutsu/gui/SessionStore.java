package net.justapie.majutsu.gui;

import net.justapie.majutsu.db.repository.user.UserRepositoryFactory;
import net.justapie.majutsu.db.schema.user.User;

public class SessionStore {
    private static final SessionStore INSTANCE = new SessionStore();

    private long currentUserId;

    private User currentUser;

    public SessionStore() {
    }

    public static SessionStore getInstance() {
        return INSTANCE;
    }

    public void setCurrentUserId(long userId) {
        this.currentUserId = userId;
    }

    public long getCurrentUserId() {
        return currentUserId;
    }

    public User fetchCurrentUser() {
        currentUser = UserRepositoryFactory.getInstance().create().getUserById(this.currentUserId);
        return currentUser;
    }

    public void clearSession() {
        this.currentUserId = 0;
    }
}
