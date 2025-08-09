package net.justapie.majutsu.gui.component;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class SelectedBook extends BaseComponent<GridPane> {
    private final String imgLink;
    private final Button button;

    public SelectedBook(String imgLink) {
        this.master = new GridPane();
        this.imgLink = imgLink;
        this.button = new Button("Del");
        this.init();
    }

    @Override
    protected void init() {
        this.master.addColumn(
                0, this.button
        );
        GridPane.setHalignment(this.button, HPos.CENTER);

        ImageView imageView = new ImageView(new Image(this.imgLink));

        this.master.addColumn(
                1, imageView
        );

        GridPane.setMargin(imageView, new Insets(0, 0, 0, 20));
    }

    public Button getButton() {
        return this.button;
    }
}
