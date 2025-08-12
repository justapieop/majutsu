package net.justapie.majutsu.gui.component;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.justapie.majutsu.db.schema.book.Book;

import java.util.ArrayList;
import java.util.List;

public abstract class BookModificationBase {
    protected Stage stage;

    protected ArrayList<Integer> selectedOptions;

    private boolean confirmation;

    @FXML
    public VBox selectionSection;

    public void createNewStage() {
        this.stage = new Stage();
        this.stage.initModality(Modality.APPLICATION_MODAL);
        this.stage.centerOnScreen();
        this.stage.sizeToScene();
        this.stage.setResizable(false);
        this.confirmation = false;
    }

    public abstract void setSelectionSection(List<Book> source);

    public void show(Scene scene) {
        this.stage.setScene(scene);
        this.stage.show();
    }

    public void terminateWindow() {
        this.stage.close();
        this.stage = null;
    }

    @FXML
    protected void setOnConfirmButton(ActionEvent event) {
        setConfirmation(true);
        terminateWindow();
    }

    @FXML
    protected void setOnCancelButton(ActionEvent event) {
        setConfirmation(false);
        terminateWindow();
    }

    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }

    public boolean isConfirmed() {
        return this.confirmation;
    }
}
