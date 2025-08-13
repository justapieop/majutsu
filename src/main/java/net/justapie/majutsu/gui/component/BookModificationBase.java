package net.justapie.majutsu.gui.component;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public abstract class BookModificationBase {
    protected Stage stage;

    protected List<Integer> selectedOptions;

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

    public void show(Scene scene) {
        this.stage.setScene(scene);
        this.stage.showAndWait();
    }

    public void terminateWindow() {
        this.stage.close();
        this.stage = null;
    }

    @FXML
    protected void setOnConfirmButton() {
        setConfirmation(true);
        terminateWindow();
    }

    @FXML
    protected void setOnCancelButton() {
        setConfirmation(false);
        terminateWindow();
    }

    public void setConfirmation(boolean confirmation) {
        this.confirmation = confirmation;
    }

    public boolean isConfirmed() {
        return this.confirmation;
    }

    public List<Integer> getSelectedOption() {
        selectedOptions.sort(Integer::compareTo);
        return selectedOptions;
    }
}
