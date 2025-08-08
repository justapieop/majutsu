package net.justapie.majutsu.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import net.justapie.majutsu.db.repository.document.BookRepositoryFactory;
import net.justapie.majutsu.db.schema.book.Book;
import net.justapie.majutsu.db.schema.user.User;
import net.justapie.majutsu.db.schema.user.UserRole;
import net.justapie.majutsu.gui.SceneManager;
import net.justapie.majutsu.gui.SceneType;
import net.justapie.majutsu.gui.SessionStore;

import java.io.IOException;
import java.net.URL;
import java.util.*;

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

    private static List<Book> borrowedBooks;
    private static List<Book> availableBooks;
    private static List<Book> expiredBooks;
    private static List<Book> unavailableBooks;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = SessionStore.getInstance().getCurrentUser();

        if (Objects.isNull(user)) {
            return;
        }

        this.comboBox.setValue("Welcome, " + user.getName() + "!");

        this.comboBox.getItems().addAll("My account", "Logout");

        if (user.getRole().equals(UserRole.ADMIN)) {
            this.comboBox.getItems().add("Switch to admin panel");
        }

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

                case "Switch to admin panel": {
                    this.onAdminSwitchClick();
                    break;
                }
            }
        });

        List<Book> bookList = BookRepositoryFactory.getInstance().create().getAllBooks();

        borrowedBooks = bookList.stream().filter((b) -> {
            return b.isBorrowed() && !isExpired(b);
        }).toList();

        availableBooks = bookList.stream().filter((b) -> {
            return b.isAvailable();
        }).toList();

        expiredBooks = bookList.stream().filter((b) -> {
            return b.isBorrowed() && isExpired(b);
        }).toList();

        unavailableBooks = bookList.stream().filter((b) -> {
            return !b.isAvailable();
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

    public static List<Book> getAvailableBooks() {
        return availableBooks;
    }

    public static List<Book> getUnavailableBooks() {
        return unavailableBooks;
    }

    private boolean activateSubWindow(String path, List<Book> source) {
        FXMLLoader loader = SceneManager.getLoader(path);
        try {
            Scene scene = new Scene(loader.load());
            BorrowBox controller = loader.getController();
            controller.createNewStage();
            controller.setSelectionSection(source);
            controller.show(scene);
            return controller.isConfirmed();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @FXML
    private void onBorrowBookClick(ActionEvent event) {
        activateSubWindow(SceneType.BORROW, availableBooks);
    }

    @FXML
    private void onReturnBookClick(ActionEvent event) {
        activateSubWindow(SceneType.RETURN, unavailableBooks);
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
        return result;
    }

    private Label categoriesLabel(Book book) {
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

    private HBox createRow(Book book) {
        HBox row = new HBox();

        row.setAlignment(Pos.CENTER);
        row.setPadding(new Insets(5, 10, 5, 10));

        Label idLabel = new Label(book.getId());
        idLabel.setPrefWidth(64);

        Label nameLabel = new Label(book.getVolumeInfo().getTitle());
        nameLabel.setPrefWidth(200);

        String currentStatus = (book.isAvailable() ? "Available" : (isExpired(book) ? "Expired" : "Borrowed"));
        Label statusLabel = new Label(currentStatus);
        statusLabel.setPrefWidth(100);

        Label bookAuthors = authorsLabel(book);
        bookAuthors.setPrefWidth(200);
        bookAuthors.setWrapText(true);

        Label bookCategories = categoriesLabel(book);
        bookCategories.setPrefWidth(300);
        bookCategories.setWrapText(true);

        row.getChildren().addAll(
                idLabel,
                nameLabel,
                bookAuthors,
                statusLabel,
                bookCategories
        );

        row.setStyle("""
                -fx-border-color: #d0d0d0;
                -fx-border-width: 1;
        """);

        return row;
    }

    private void onAdminSwitchClick() {
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
