package net.justapie.majutsu.gui.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import net.justapie.majutsu.db.repository.user.UserRepositoryFactory;
import net.justapie.majutsu.db.schema.user.User;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminUserController extends AdminBookController implements Initializable {
    private final List<User> selectedUsers = new ArrayList<>();
    private List<User> users;
    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, CheckBox> selectCol;

    @FXML
    private TableColumn<User, Long> uidCol;

    @FXML
    private TableColumn<User, String> uNameCol;

    @FXML
    private TableColumn<User, String> uEmailCol;

    @FXML
    private TableColumn<User, String> uRoleCol;

    @FXML
    private TableColumn<User, String> uCreatedAtCol;

    @FXML
    private TableColumn<User, String> uActiveCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.users = UserRepositoryFactory.getInstance().create().getAllUsers();
        this.userTable.getItems().addAll(this.users);
        this.setupUserColumns();
    }

    private void setupUserColumns() {
        this.selectCol.setCellValueFactory(
                c -> {
                    CheckBox cb = new CheckBox();
                    cb.setText("");
                    cb.setOnAction(e -> {
                        if (cb.isSelected()) {
                            this.selectedUsers.add(c.getValue());
                        }

                        if (!cb.isSelected()) {
                            this.selectedUsers.remove(c.getValue());
                        }
                    });
                    return new SimpleObjectProperty<>(cb);
                }
        );

        this.uidCol.setCellValueFactory(
                c -> new SimpleObjectProperty<>(c.getValue().getId())
        );

        this.uNameCol.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getName())
        );

        this.uEmailCol.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getEmail())
        );

        this.uRoleCol.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getRole().toString())
        );

        this.uCreatedAtCol.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getCreatedAt().toString())
        );

        this.uActiveCol.setCellValueFactory(
                c -> new SimpleStringProperty(
                        c.getValue().isActive() ? "Yes" : "No"
                )
        );
    }
}
