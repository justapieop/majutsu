package net.justapie.majutsu.gui.controller;

import ch.qos.logback.classic.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import net.justapie.majutsu.db.repository.book.BookRepositoryFactory;
import net.justapie.majutsu.db.schema.book.Book;
import net.justapie.majutsu.db.schema.user.User;
import net.justapie.majutsu.db.schema.user.UserRole;
import net.justapie.majutsu.gui.SceneManager;
import net.justapie.majutsu.gui.SceneType;
import net.justapie.majutsu.gui.SessionStore;
import net.justapie.majutsu.gui.component.BoxInteractive;
import net.justapie.majutsu.gui.component.GUIComponent;
import net.justapie.majutsu.gui.controller.prep.DataPreprocessing;
import net.justapie.majutsu.utils.Utils;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class DashboardController extends BaseController implements Initializable {
    private static final Logger LOGGER = Utils.getInstance().getRootLogger().getLoggerContext().getLogger(DashboardController.class);

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private Button borrowedBooksPrompt;

    @FXML
    private Button availableBooksPrompt;

    @FXML
    private Button expiredBooksPrompt;

    @FXML
    private VBox availableBookContainer;

    @FXML
    private VBox unavailableBookContainer;

    private List<Book> borrowedBooks;
    private List<Book> availableBooks;
    private List<Book> expiredBooks;
    private List<Book> unavailableBooks;

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

        borrowedBooks = new ArrayList<>(bookList.stream().filter(
                (book) -> !book.isAvailable() && !DataPreprocessing.isExpired(book)).toList()
        );

        availableBooks = new ArrayList<>(bookList.stream().filter(Book::isAvailable).toList());

        expiredBooks = new ArrayList<>(bookList.stream().filter(
                (book) -> !book.isAvailable() && DataPreprocessing.isExpired(book)).toList())
        ;

        unavailableBooks = new ArrayList<>(bookList.stream().filter(
                (book) -> !book.isAvailable()).toList()
        );

        availableBookContainer.setPadding(new Insets(5, 5, 5, 5));
        availableBookContainer.setSpacing(5);

        refresh();
    }

    private void refresh() {

        // Insert here init functions for numbers.
        Integer numberOfBorrowedBooks = borrowedBooks.size();
        Integer numberOfAvailableBooks = availableBooks.size();
        Integer numberOfExpiredBooks = expiredBooks.size();

        this.borrowedBooksPrompt.setText(String.format("Number of borrowed books: %d", numberOfBorrowedBooks));
        this.borrowedBooksPrompt.setStyle("");

        this.availableBooksPrompt.setText(String.format("Number of available books: %d", numberOfAvailableBooks));
        this.expiredBooksPrompt.setText(String.format("Number of expired books: %d", numberOfExpiredBooks));

        availableBookContainer.getChildren().clear();
        for (Book book : availableBooks) {
            availableBookContainer.getChildren().add(GUIComponent.createRow(book));
        }

        unavailableBookContainer.getChildren().clear();
        for (Book book : unavailableBooks) {
            unavailableBookContainer.getChildren().add(GUIComponent.createRow(book));
        }
    }

    private List<Integer> activateSubWindow(String path, List<Book> source) {
        FXMLLoader loader = SceneManager.getLoader(path);
        try {
            Scene scene = new Scene(loader.load());
            BoxInteractive controller = loader.getController();
            controller.createNewStage();
            controller.setSelectionSection(source);
            controller.show(scene);
            if (controller.isConfirmed()) {
                return controller.getSelectedOption();
            }
        }
        catch (IOException e) {
            LOGGER.error("Failed while getting active subwindow");
            LOGGER.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    @FXML
    private void onBorrowBookClick() {
        List<Integer> modification = activateSubWindow(SceneType.BORROW, availableBooks);
        for (int i = modification.size() - 1; i >= 0; i--) {
            int index = modification.get(i);
            Book book = availableBooks.get(index);
            borrowedBooks.add(book);
            unavailableBooks.add(book);
            availableBooks.remove(index);
        }
        refresh();
    }

    @FXML
    private void onReturnBookClick() {
        List<Integer> modification = activateSubWindow(SceneType.RETURN, unavailableBooks);
        for (int i = modification.size() - 1; i >= 0; i--) {
            int index = modification.get(i);
            Book book = unavailableBooks.get(index);
            availableBooks.add(book);
            borrowedBooks.remove(book);
            expiredBooks.remove(book);
            unavailableBooks.remove(index);
        }
        refresh();
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
