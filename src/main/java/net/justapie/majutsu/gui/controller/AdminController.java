package net.justapie.majutsu.gui.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import net.justapie.majutsu.db.repository.book.BookRepositoryFactory;
import net.justapie.majutsu.db.schema.book.Book;
import net.justapie.majutsu.gbook.model.Volume;
import net.justapie.majutsu.gui.SceneType;
import net.justapie.majutsu.gui.model.DisplayableBook;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController extends BaseController implements Initializable {
    private final List<DisplayableBook> selectedBooks = new ArrayList<>();

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
    private TextField bookSearchTextField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setupBookColumns();

        List<Book> books = BookRepositoryFactory.getInstance().create().getAllBooks();

        for (final Book book : books) {
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
    private void onBlock() {
        BookRepositoryFactory.getInstance().create().setBookAvailability(
                true, this.selectedBooks.stream().map(Volume::getId).toList()
        );
    }

    @FXML
    private void onAllow() {
        BookRepositoryFactory.getInstance().create().setBookAvailability(
                true, this.selectedBooks.stream().map(Volume::getId).toList()
        );
    }

    @FXML
    private void onRemove() {
        BookRepositoryFactory.getInstance().create().remove(
                this.selectedBooks.stream().map(Volume::getId).toList()
        );
    }

    @FXML
    private void onBackClick() {
        this.switchToScene(SceneType.DASHBOARD);
    }

    protected void setupBookColumns() {
        this.bookSelectCol.setCellValueFactory(
                c -> {
                    c.getValue().getCheckBox().setOnAction(e -> {
                        if (c.getValue().getCheckBox().isSelected()) {
                            this.selectedBooks.add(c.getValue());
                        }

                        if (!c.getValue().getCheckBox().isSelected()) {
                            this.selectedBooks.remove(c.getValue());
                        }
                    });
                    return new SimpleObjectProperty<>(c.getValue().getCheckBox());
                }
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
    }
}
