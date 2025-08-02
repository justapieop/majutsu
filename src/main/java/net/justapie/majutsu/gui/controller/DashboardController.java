package net.justapie.majutsu.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.justapie.majutsu.db.schema.user.User;
import net.justapie.majutsu.db.schema.user.UserRole;
import net.justapie.majutsu.gui.SceneManager;
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

    @FXML
    private VBox availableBookContainer;

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

//        GBookClient.getInstance().getVolumeById()

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
    }

//    @FXML
//    private void onAddBookClick(ActionEvent event) {
//        availableBookContainer.getChildren().add(createRow());
//    }

    @FXML
    private void onBorrowBookClick(ActionEvent event) {
        SceneManager.triggerSubWindow(SceneManager.loadScene(SceneType.BORROW));
    }

    @FXML
    private void onReturnBookClick(ActionEvent event) {
        SceneManager.triggerSubWindow(SceneManager.loadScene(SceneType.RETURN));
    }

    private HBox createRow() {
        HBox row = new HBox();

        row.setAlignment(Pos.CENTER);
        row.setPadding(new Insets(5, 10, 5, 10));

        Label idLabel = new Label("ID");
        idLabel.setPrefWidth(64);

        Label nameLabel = new Label("Name");
        nameLabel.setPrefWidth(200);

        Label statusLabel = new Label("Status");
        statusLabel.setPrefWidth(100);

        Label modifiedLabel = new Label("Last Modified");
        modifiedLabel.setPrefWidth(120);

        row.getChildren().addAll(
                idLabel,
                nameLabel,
                modifiedLabel,
                statusLabel
        );

        row.setStyle("""
                -fx-border-color: #d0d0d0;
                -fx-border-width: 1;
        """);

        return row;
    }

    @FXML
    private void onAdminSwitchClick(ActionEvent event) {
        User user = SessionStore.getInstance().getCurrentUser();

        if (Objects.isNull(user) || !user.getRole().equals(UserRole.ADMIN) || true) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Announcement!");
            alert.setHeaderText("Access denied!");
            alert.setContentText("Describe problem: Authorization violation!");
            alert.show();
        }
        else {
            new AdminSplashController().process();
        }
    }

}
