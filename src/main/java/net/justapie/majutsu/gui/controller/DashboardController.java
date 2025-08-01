package net.justapie.majutsu.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import net.justapie.majutsu.db.schema.user.User;
import net.justapie.majutsu.db.schema.user.UserRole;
import net.justapie.majutsu.gui.SceneType;
import net.justapie.majutsu.gui.SessionStore;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class DashboardController extends BaseController implements Initializable {
    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private Label borrowedBooksPrompt;

    private Integer numberOfBorrowedBooks;

    @FXML
    private Label availableBooksPrompt;

    private Integer numberOfAvailableBooks;

    @FXML
    private Label expiredBooksPrompt;

    @FXML
    private Button adminSwitchBtn;

    private Integer numberOfExpiredBooks;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = SessionStore.getInstance().getCurrentUser();

        if (Objects.isNull(user)) {
            return;
        }

        this.comboBox.setValue("Welcome, " + user.getName() + "!");

        this.comboBox.getItems().add("My account");
        this.comboBox.getItems().add("Logout");

        this.comboBox.setOnAction(event -> {
            String selectedItem = this.comboBox.getSelectionModel().getSelectedItem();

            switch (selectedItem) {
                case "My account": {
                    this.switchToScene(SceneType.ACCOUNT);
                    break;
                }

                case "Logout": {
                    SessionStore.getInstance().clearSession();
                    this.switchToScene(SceneType.LOGIN);
                    break;
                }
            }
        });

        // Insert here init functions for numbers.
        numberOfBorrowedBooks = 10;
        numberOfAvailableBooks = 20;
        numberOfExpiredBooks = 30;

        if (numberOfBorrowedBooks == null) {
            borrowedBooksPrompt.setText("Number of borrowed books: loading...");
        } else {
            borrowedBooksPrompt.setText(String.format("Number of borrowed books: %d.", numberOfBorrowedBooks));
        }

        if (numberOfAvailableBooks == null) {
            availableBooksPrompt.setText("Number of available books: loading...");
        }
        else {
            availableBooksPrompt.setText(String.format("Number of available books: %d.", numberOfAvailableBooks));
        }

        if (numberOfExpiredBooks == null) {
            expiredBooksPrompt.setText("Number of expired books: loading...");
        }
        else {
            expiredBooksPrompt.setText(String.format("Number of expired books: %d.", numberOfExpiredBooks));
        }

        if (!user.getRole().equals(UserRole.ADMIN)) {
            this.adminSwitchBtn.setVisible(false);
        }
    }

    @FXML
    private void onAdminSwitchClick(ActionEvent event) {
        new AdminSplashController().process();
    }
}
