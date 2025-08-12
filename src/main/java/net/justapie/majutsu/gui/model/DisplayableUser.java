package net.justapie.majutsu.gui.model;

import net.justapie.majutsu.db.schema.user.User;

public class DisplayableUser extends User {
    public static DisplayableUser fromUser(User user) {
        DisplayableUser usr = new DisplayableUser();

        usr.id = user.getId();
        usr.name = user.getName();
        usr.email = user.getEmail();
        usr.hashedPassword = user.getHashedPassword();
        usr.role = user.getRole();
        usr.active = user.isActive();
        usr.borrowedBooks = user.getBorrowedBooks();
        usr.createdAt = user.getCreatedAt();

        return usr;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
