package net.justapie.majutsu.gui.component;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import net.justapie.majutsu.gui.model.DisplayableBook;

import java.util.Objects;

public class BookSelect extends BaseComponent<GridPane> {
    private final DisplayableBook book;

    public BookSelect(DisplayableBook book) {
        super();
        this.book = book;
        this.master = new GridPane();
        this.init();
    }

    @Override
    protected void init() {
        GridPane textPane = new GridPane();
        textPane.setPadding(new Insets(0, 0, 0, 20));

        Label title = new Label(this.book.getVolumeInfo().getTitle());
        title.setWrapText(true);

        Label author = new Label(
                "by " + String.join(", ", this.book.getVolumeInfo().getAuthors())
        );
        author.setWrapText(true);

        Label desc = new Label(
                this.book.getVolumeInfo().getDescription()
        );
        desc.setWrapText(true);

        Label language = new Label(
                "Language: " + this.book.getVolumeInfo().getLanguage()
        );

        title.setStyle("-fx-font-weight: bold;");

        textPane.addRow(
                0, title
        );

        textPane.addRow(
                1, author
        );

        if (!Objects.isNull(desc.getText()) && !desc.getText().isBlank()) {
            textPane.addRow(
                    2, desc
            );
        }

        textPane.addRow(
                3, language
        );

        this.master.addColumn(
                0,
                this.book.getCheckBox()
        );

        // Handle null image links gracefully
        ImageView imgView;
        if (this.book.getVolumeInfo().getImageLinks() != null && 
            this.book.getVolumeInfo().getImageLinks().getThumbnail() != null) {
            imgView = new ImageView(
                    new Image(this.book.getVolumeInfo().getImageLinks().getThumbnail())
            );
        } else {
            imgView = new ImageView();
            imgView.setFitWidth(64);
            imgView.setFitHeight(64);
        }

        this.book.getCheckBox().setPadding(new Insets(0, 20, 0, 0));

        this.master.addColumn(
                1, imgView
        );

        this.master.addColumn(
                2, textPane
        );
    }
}
