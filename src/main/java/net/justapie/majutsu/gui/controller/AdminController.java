package net.justapie.majutsu.gui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import net.justapie.majutsu.db.repository.document.DocumentRepositoryFactory;
import net.justapie.majutsu.db.schema.book.Book;
import net.justapie.majutsu.db.schema.user.User;
import net.justapie.majutsu.db.schema.user.UserRole;
import net.justapie.majutsu.gui.SceneType;
import net.justapie.majutsu.gui.SessionStore;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminController extends BaseController implements Initializable {
    @FXML
    private TableView bookTable;

    @FXML
    private TableColumn idCol;

    @FXML
    private TableColumn nameCol;

    @FXML
    private TableColumn borrowedCol;

    @FXML
    private TableColumn borrowedAtCol;

    @FXML
    private TableColumn expectedReturnCol;

    @FXML
    private TableColumn returnDateCol;

    @FXML
    private TableColumn borrowedByCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Book> books = DocumentRepositoryFactory.getInstance().create().getAllBooks();

        System.out.println(books.size());
    }

    @FXML
    private void onBackClick(ActionEvent event) {
        this.switchToScene(SceneType.DASHBOARD);
    }
}
