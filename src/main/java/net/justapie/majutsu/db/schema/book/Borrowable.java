package net.justapie.majutsu.db.schema.book;


public interface Borrowable {
    boolean borrow(String user);
    boolean borrow(String user, int days);
    void returnItem();
    boolean isBorrowed();
    boolean isAvailable();
}
