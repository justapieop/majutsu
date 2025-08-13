package net.justapie.majutsu.gui.component;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import net.justapie.majutsu.db.schema.book.Book;
import net.justapie.majutsu.gui.controller.DashboardController;

public class GUIComponent {
    private static Label authorsLabel(Book book) {
        String text = new String();
        for (final String author : book.getVolumeInfo().getAuthors()) {
            text += author + ", ";
        }
        Label result = new Label();
        if (!text.isBlank() && text.length() > 1) {
            result.setText(text.substring(0, text.length() - 2) + ".");
        }
        return result;
    }

    private static Label categoriesLabel(Book book) {
        String text = new String();
        for (final String category : book.getVolumeInfo().getCategories()) {
            text += category + ", ";
        }
        Label result = new Label();
        if (!text.isBlank() && text.length() > 1) {
            result.setText(text.substring(0, text.length() - 2) + ".");
        }
        return result;
    }

    public static HBox createRow(Book book) {
        HBox row = new HBox();

        Label nameLabel = new Label(book.getVolumeInfo().getTitle());
        nameLabel.setPrefWidth(300);
        nameLabel.setWrapText(true);
        nameLabel.setAlignment(Pos.CENTER);
//        nameLabel.setStyle("""
//                -fx-border-color: #d0d0d0;
//                -fx-border-width: 1;
//        """);

        String currentStatus = (book.isAvailable() ? "Available" : (DashboardController.isExpired(book) ? "Expired" : "Borrowed"));
        Label statusLabel = new Label(currentStatus);
        statusLabel.setPrefWidth(100);
        statusLabel.setAlignment(Pos.CENTER);
//        statusLabel.setStyle("""
//                -fx-border-color: #d0d0d0;
//                -fx-border-width: 1;
//        """);

        Label bookAuthors = authorsLabel(book);
        bookAuthors.setPrefWidth(200);
        bookAuthors.setAlignment(Pos.CENTER);
//        bookAuthors.setStyle("""
//                -fx-border-color: #d0d0d0;
//                -fx-border-width: 1;
//        """);

        Label categories = categoriesLabel(book);
        categories.setPrefWidth(200);
        categories.setAlignment(Pos.CENTER);
//        categories.setStyle("""
//                -fx-border-color: #d0d0d0;
//                -fx-border-width: 1;
//        """);

        row.getChildren().addAll(
                nameLabel,
                bookAuthors,
                categories,
                statusLabel
        );

        row.setStyle("""
                -fx-border-color: #d0d0d0;
                -fx-border-width: 1;
        """);
        row.setPrefHeight(36);
        row.setSpacing(18);
        row.setAlignment(Pos.CENTER);
        row.setPadding(new Insets(3, 5, 3, 5));

        return row;
    }
}
