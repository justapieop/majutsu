package net.justapie.majutsu.gui.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import net.justapie.majutsu.cache.Cache;
import net.justapie.majutsu.db.schema.book.Book;
import net.justapie.majutsu.gui.SceneType;
import net.justapie.majutsu.gui.model.DisplayableBook;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminController extends BaseController implements Initializable {
    private ArrayList<Book> books;

    @FXML
    private TableView<DisplayableBook> bookTable;

    @FXML
    private TableColumn<DisplayableBook, CheckBox> bookSelectCol;

    @FXML
    private TableColumn<DisplayableBook, String> idCol;

    @FXML
    private TableColumn<DisplayableBook, String> nameCol;

    @FXML
    private TableColumn<DisplayableBook, String> createdAtCol;

    @FXML
    private TableColumn<DisplayableBook, String> availableCol;

    @FXML
    private TableColumn<DisplayableBook, String> borrowedCol;

    @FXML
    private TableColumn<DisplayableBook, String> borrowedAtCol;

    @FXML
    private TableColumn<DisplayableBook, String> expectedReturnCol;

    @FXML
    private TableColumn<DisplayableBook, String> returnDateCol;

    @FXML
    private TableColumn<DisplayableBook, String> borrowedByCol;

    @FXML
    private TextField bookSearchTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setupBookColumns();

        this.books = Cache.getInstance().<ArrayList<Book>>get("books").getData();

        for (final Book book : this.books) {
            this.bookTable.getItems().add(
                    DisplayableBook.fromBook(book)
            );
        }
    }

    @FXML
    private void onAdminBookAdd() {
        this.switchToScene(SceneType.ADMIN_BOOK_SEARCH);
    }

    @FXML
    private void onBookSearchCommit() {

    }

    @FXML
    private void onBackClick() {
        this.switchToScene(SceneType.DASHBOARD);
    }

    protected void setupBookColumns() {
        this.bookSelectCol.setCellValueFactory(
                c -> new SimpleObjectProperty<>(c.getValue().getCheckBox())
        );
        this.idCol.setCellValueFactory(
                c -> new SimpleStringProperty(
                        c.getValue().getId()
                )
        );
        this.nameCol.setCellValueFactory(
                c -> new SimpleStringProperty(
                        c.getValue().getVolumeInfo().getTitle()
                )
        );
        this.createdAtCol.setCellValueFactory(
                c -> new SimpleStringProperty(
                        c.getValue().getCreatedAt().toString()
                )
        );
        this.availableCol.setCellValueFactory(
                c -> new SimpleStringProperty(
                        c.getValue().isAvailable() ? "Yes" : "No"
                )
        );
        this.borrowedAtCol.setCellValueFactory(
                c -> new SimpleStringProperty(
                        c.getValue().getBorrowedAt().toString()
                )
        );
        this.borrowedCol.setCellValueFactory(
                c -> new SimpleStringProperty(
                        c.getValue().isBorrowed() ? "Yes" : "No"
                )
        );
        this.expectedReturnCol.setCellValueFactory(
                c -> new SimpleStringProperty(
                        c.getValue().expectedReturn().toString()
                )
        );
        this.returnDateCol.setCellValueFactory(
                c -> new SimpleStringProperty(
                        c.getValue().getReturnDate().toString()
                )
        );
        this.borrowedByCol.setCellValueFactory(
                c -> new SimpleStringProperty(
                        String.valueOf(c.getValue().getBorrowedBy())
                )
        );
    }
}
