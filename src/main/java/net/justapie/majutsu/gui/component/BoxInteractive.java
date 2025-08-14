package net.justapie.majutsu.gui.component;

import javafx.scene.Scene;
import net.justapie.majutsu.db.schema.book.Book;

import java.util.List;

public interface BoxInteractive {
    void createNewStage();
    void show(Scene scene);
    boolean isConfirmed();
    void setSelectionSection(List<Book> source);
    List<Integer> getSelectedOption();
}
