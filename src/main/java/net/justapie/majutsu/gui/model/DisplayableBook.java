package net.justapie.majutsu.gui.model;

import javafx.scene.control.CheckBox;
import net.justapie.majutsu.db.schema.book.Book;

public class DisplayableBook extends Book {
    private final CheckBox checkBox;

    protected DisplayableBook() {
        super();
        this.checkBox = new CheckBox();
    }

    public static DisplayableBook fromBook(Book book) {
        DisplayableBook displayableBook = new DisplayableBook();

        displayableBook.id = book.getId();
        displayableBook.volumeInfo = book.getVolumeInfo();
        displayableBook.borrowed = book.isBorrowed();
        displayableBook.borrowedAt = book.getBorrowedAt();
        displayableBook.borrowedBy = book.getBorrowedBy();
        displayableBook.returnDate = book.getReturnDate();
        displayableBook.expectedReturn = book.expectedReturn();
        displayableBook.available = book.isAvailable();
        displayableBook.createdAt = book.getCreatedAt();

        return displayableBook;
    }

    public CheckBox getCheckBox() {
        return this.checkBox;
    }
}
