package net.justapie.majutsu.gui.controller;

import ch.qos.logback.classic.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import net.justapie.majutsu.db.repository.book.BookRepositoryFactory;
import net.justapie.majutsu.db.repository.user.UserRepositoryFactory;
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

        if (user.isFirstLogin()) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("This is your first login with predefined password. Please change your password to continue");
                Optional<ButtonType> type = alert.showAndWait();

                type.ifPresentOrElse((t) ->
                        Platform.runLater(() -> this.switchToScene(SceneType.ACCOUNT))
                        , () -> Platform.runLater(() -> this.switchToScene(SceneType.ACCOUNT))
                );
            });
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

        this.borrowedBooks = user.getBorrowedBooks();
        this.availableBooks = new ArrayList<>(bookList.stream().filter(Book::isAvailable).toList());

        this.expiredBooks = new ArrayList<>(bookList.stream().filter(
                (book) -> !book.isAvailable() && DataPreprocessing.isExpired(book)).toList()
        );

        this.unavailableBooks = new ArrayList<>(bookList.stream().filter(
                (book) -> !book.isAvailable()).toList()
        );

        this.availableBookContainer.setPadding(new Insets(5, 5, 5, 5));
        this.availableBookContainer.setSpacing(5);

        refresh();
    }

    private void refresh() {

        // Insert here init functions for numbers.
        Integer numberOfBorrowedBooks = this.borrowedBooks.size();
        Integer numberOfAvailableBooks = this.availableBooks.size();
        Integer numberOfExpiredBooks = this.expiredBooks.size();

        this.borrowedBooksPrompt.setText(String.format("Number of borrowed books: %d", numberOfBorrowedBooks));
        this.borrowedBooksPrompt.setStyle("");

        this.availableBooksPrompt.setText(String.format("Number of available books: %d", numberOfAvailableBooks));
        this.expiredBooksPrompt.setText(String.format("Number of expired books: %d", numberOfExpiredBooks));

        availableBookContainer.getChildren().clear();
        for (Book book : this.availableBooks) {
            availableBookContainer.getChildren().add(GUIComponent.createRow(book));
        }

        unavailableBookContainer.getChildren().clear();
        for (Book book : this.unavailableBooks) {
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
        List<Integer> modification = activateSubWindow(SceneType.BORROW, this.availableBooks);
        for (int i = modification.size() - 1; i >= 0; i--) {
            int index = modification.get(i);
            Book book = this.availableBooks.get(index);

            UserRepositoryFactory.getInstance().create()
                    .borrowBook(
                            SessionStore.getInstance().getCurrentUser().getId(),
                            book.getId()
                    );

            this.borrowedBooks.add(book);
            this.unavailableBooks.add(book);
            this.availableBooks.remove(index);
        }

        refresh();
    }

    @FXML
    private void onReturnBookClick() {
        List<Integer> modification = activateSubWindow(SceneType.RETURN, this.unavailableBooks);
        for (int i = modification.size() - 1; i >= 0; i--) {
            int index = modification.get(i);
            Book book = this.unavailableBooks.get(index);
            UserRepositoryFactory.getInstance().create()
                    .returnBook(
                            SessionStore.getInstance().getCurrentUser().getId(),
                            book.getId()
                    );
            this.availableBooks.add(book);
            this.borrowedBooks.remove(book);
            this.expiredBooks.remove(book);
            this.unavailableBooks.remove(index);
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
