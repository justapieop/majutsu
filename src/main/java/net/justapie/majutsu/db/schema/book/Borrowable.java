package net.justapie.majutsu.db.schema.book;

import java.time.LocalDate;

public interface Borrowable {
    boolean borrow(String user);
    boolean returnItem();
    boolean isBorrowed();
    String getBorrowedBy();
    LocalDate getBorrowedAt();
    LocalDate getDueDate();
}
