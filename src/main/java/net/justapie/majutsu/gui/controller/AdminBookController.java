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

public class AdminBookController extends BaseController implements Initializable {
    protected final List<DisplayableBook> selectedBooks = new ArrayList<>();
    protected List<Book> books;

    @FXML
    protected TableView<DisplayableBook> bookTable;

    @FXML
    protected TableColumn<DisplayableBook, CheckBox> bookSelectCol;

    @FXML
    protected TableColumn<DisplayableBook, String> idCol;

    @FXML
    protected TableColumn<DisplayableBook, String> nameCol;

    @FXML
    protected TableColumn<DisplayableBook, String> createdAtCol;

    @FXML
    protected TableColumn<DisplayableBook, String> availableCol;

    @FXML
    protected TextField bookSearchField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.setupBookColumns();

        this.books = BookRepositoryFactory.getInstance().create().getAllBooks();

        this.bookTable.getItems().addAll(this.books.stream().map(
                DisplayableBook::fromBook
        ).toList());
    }

    @FXML
    protected void onAdminBookAdd() {
        this.switchToScene(SceneType.ADMIN_BOOK_SEARCH);
    }

    @FXML
    protected void onBookSearchCommit() {
        String query = this.bookSearchField.getText();

        this.bookTable.getItems().clear();

        if (query.isBlank()) {
            this.bookTable.getItems().addAll(this.books.stream().map(
                    DisplayableBook::fromBook
            ).toList());
            return;
        }


        List<Book> filtered = this.books.stream().toList();
    }

    @FXML
    protected void onBlock() {
        BookRepositoryFactory.getInstance().create().setBookAvailability(
                false, this.selectedBooks.stream().map(
                        v -> {
                            int idx = this.bookTable.getItems().indexOf(v);
                            v.setAvailable(false);
                            this.bookTable.getItems().set(idx, v);
                            return v.getId();
                        }
                ).toList()
        );
    }

    @FXML
    protected void onAllow() {
        BookRepositoryFactory.getInstance().create().setBookAvailability(
                true, this.selectedBooks.stream().map(
                        v -> {
                            int idx = this.bookTable.getItems().indexOf(v);
                            v.setAvailable(true);
                            this.bookTable.getItems().set(idx, v);
                            return v.getId();
                        }
                ).toList()
        );
    }

    @FXML
    protected void onRemove() {
        BookRepositoryFactory.getInstance().create().remove(
                this.selectedBooks.stream().map(Volume::getId).toList()
        );

        this.selectedBooks.forEach(
                v -> {
                    this.bookTable.getItems().remove(v);
                }
        );
        this.selectedBooks.clear();
    }

    @FXML
    protected void onBackClick() {
        this.switchToScene(SceneType.DASHBOARD);
    }

    private void setupBookColumns() {
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
