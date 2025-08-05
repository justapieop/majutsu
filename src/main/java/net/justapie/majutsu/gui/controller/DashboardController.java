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
import net.justapie.majutsu.db.repository.document.DocumentRepositoryFactory;
import net.justapie.majutsu.db.schema.book.Book;
import net.justapie.majutsu.db.schema.user.User;
import net.justapie.majutsu.db.schema.user.UserRole;
import net.justapie.majutsu.gui.SceneManager;
import net.justapie.majutsu.gui.SceneType;
import net.justapie.majutsu.gui.SessionStore;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

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

        List<Book> bookList = DocumentRepositoryFactory.getInstance().create().getAllBooks();

//        Book book = bookList.getFirst();
//        book.getVolumeInfo().getTitle();
//        book.getId()

        List<Book> borrowedBooks = bookList.stream().filter((b) -> {
            return b.isBorrowed() && !isExpired(b);
        }).toList();

        List<Book> availableBooks = bookList.stream().filter((b) -> {
            return b.isAvailable();
        }).toList();

        List<Book> expiredBooks = bookList.stream().filter((b) -> {
            return b.isBorrowed() && isExpired(b);
        }).toList();

        // Insert here init functions for numbers.
        this.numberOfBorrowedBooks = borrowedBooks.size();
        this.numberOfAvailableBooks = availableBooks.size();
        this.numberOfExpiredBooks = expiredBooks.size();

        this.borrowedBooksPrompt.setText(String.format("Number of borrowed books: %d.", numberOfBorrowedBooks));
        this.availableBooksPrompt.setText(String.format("Number of available books: %d.", numberOfAvailableBooks));
        this.expiredBooksPrompt.setText(String.format("Number of expired books: %d.", numberOfExpiredBooks));
    }

//    @FXML
//    private void onAddBookClick(ActionEvent event) {
//        availableBookContainer.getChildren().add(createRow());
//    }

    @FXML
    private void onBorrowBookClick(ActionEvent event) {
        SceneManager.triggerSubWindow(SceneManager.loadScene(SceneType.BORROW), BorrowBox.getInstance());
    }

    @FXML
    private void onReturnBookClick(ActionEvent event) {
        SceneManager.triggerSubWindow(SceneManager.loadScene(SceneType.RETURN), ReturnBox.getInstance());
    }

    private boolean isExpired(Book book) {
        return new Date().compareTo(book.expectedReturn())  < 0;
    }

    private Label authorsLabel(Book book) {
        String text = new String();
        for (final String author : book.getVolumeInfo().getAuthors()) {
            text += author + ", ";
        }
        Label result = new Label();
        if (!text.isBlank() && text.length() > 1) {
            result.setText(text.substring(0, text.length() - 2) + ".");
        }
        result.setWrapText(true);
        return result;
    }

    private HBox createRow(Book book) {
        HBox row = new HBox();

        row.setAlignment(Pos.CENTER);
        row.setPadding(new Insets(5, 10, 5, 10));

        Label idLabel = new Label(book.getId());
        idLabel.setPrefWidth(64);

        Label nameLabel = new Label(book.getVolumeInfo().getTitle());
        nameLabel.setPrefWidth(200);
        nameLabel.setWrapText(true);

        String currentStatus = (book.isAvailable() ? "Available" : (isExpired(book) ? "Expired" : "Borrowed"));
        Label statusLabel = new Label(currentStatus);
        statusLabel.setPrefWidth(100);

        Label bookAuthors = authorsLabel(book);
        bookAuthors.setPrefWidth(200);

        row.getChildren().addAll(
                idLabel,
                nameLabel,
                bookAuthors,
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

        if (Objects.isNull(user) || !user.getRole().equals(UserRole.ADMIN)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Access denied!");
            alert.show();
        } else {
            new AdminSplashController().process();
        }
    }

}
