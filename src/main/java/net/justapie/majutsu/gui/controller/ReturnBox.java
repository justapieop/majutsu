package net.justapie.majutsu.gui.controller;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import net.justapie.majutsu.db.schema.book.Book;

import java.util.ArrayList;
import java.util.List;

public class ReturnBox extends BookModificationBase {
    @Override
    public void setSelectionSection(List<Book> source) {
        selectedOptions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CheckBox box = new CheckBox(String.format("%s: %s", i, i));
            box.setAlignment(Pos.CENTER_LEFT);
            final int index = i;
            box.setOnAction(e -> {
                if (box.isSelected()) {
                    System.out.println("Siuuuuuuu" + index);
                }
                else {
                    System.out.println("Undo siuuuuu" + index);
                }
            });
            selectionSection.getChildren().add(box);
        }
        for (int i = 0; i < source.size(); i++) {
            final Book book = source.get(i);
            CheckBox box = new CheckBox(String.format("%s: %s.", book.getId(), book.getVolumeInfo().getTitle()));
            box.setAlignment(Pos.CENTER_LEFT);
            final int index = i;
            box.setOnAction(e -> {
                if (box.isSelected()) {
                    selectedOptions.add(index);
                }
                else {
                    selectedOptions.remove(index);
                }
            });
            selectionSection.getChildren().add(box);
        }
    }
}
