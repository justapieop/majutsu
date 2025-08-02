package net.justapie.majutsu.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;

public class BorrowBox implements Selectable {
    private static final BorrowBox INSTANCE = new BorrowBox();
    private Stage currentStage;

    public static BorrowBox getInstance() {
        return INSTANCE;
    }

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }

    public void terminateWindow() {
        currentStage.close();
        currentStage = null;
    }

    public BorrowBox() {
    }

    @FXML
    public void setOnConfirmButton(ActionEvent event) {

        getInstance().terminateWindow();
    }
}
