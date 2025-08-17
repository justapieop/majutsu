package net.justapie.majutsu.gui.model;

import javafx.scene.control.CheckBox;
import net.justapie.majutsu.db.schema.book.Book;
import net.justapie.majutsu.gbook.model.Volume;

import java.util.Objects;

public class DisplayableBook extends Book {
    private final CheckBox checkBox;

    protected DisplayableBook() {
        super();
        this.checkBox = new CheckBox();
        this.checkBox.setText("");
    }

    public static DisplayableBook fromBook(Book book) {
        DisplayableBook displayableBook = new DisplayableBook();

        displayableBook.id = book.getId();
        displayableBook.volumeInfo = book.getVolumeInfo();
        displayableBook.available = book.isAvailable();
        displayableBook.createdAt = book.getCreatedAt();

        return displayableBook;
    }

    public static DisplayableBook fromVolume(Volume volume) {
        return DisplayableBook.fromBook(Book.fromVolume(volume));
    }

    public CheckBox getCheckBox() {
        return this.checkBox;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DisplayableBook that = (DisplayableBook) o;
        return Objects.equals(this.id, that.getId());
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
