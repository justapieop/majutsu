package net.justapie.majutsu.gui.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import net.justapie.majutsu.db.repository.user.UserRepositoryFactory;
import net.justapie.majutsu.gui.SceneType;
import net.justapie.majutsu.gui.SessionStore;
import net.justapie.majutsu.gui.model.DisplayableUser;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public final class AdminUserController extends AdminBookController implements Initializable {
    private final List<DisplayableUser> selectedUsers = new ArrayList<>();
    @FXML
    private TableView<DisplayableUser> userTable;

    @FXML
    private TableColumn<DisplayableUser, CheckBox> selectCol;

    @FXML
    private TableColumn<DisplayableUser, Long> uidCol;

    @FXML
    private TableColumn<DisplayableUser, String> uNameCol;

    @FXML
    private TableColumn<DisplayableUser, String> uEmailCol;

    @FXML
    private TableColumn<DisplayableUser, String> uRoleCol;

    @FXML
    private TableColumn<DisplayableUser, String> uCreatedAtCol;

    @FXML
    private TableColumn<DisplayableUser, String> uActiveCol;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        List<DisplayableUser> users = UserRepositoryFactory.getInstance().create().getAllUsers().stream().map(
                DisplayableUser::fromUser
        ).toList();
        this.userTable.getItems().addAll(users);
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
                    if (c.getValue().getId() == SessionStore.getInstance().getCurrentUser().getId()) {
                        cb.setDisable(true);
                    }
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

    @FXML
    private void onUserBlock() {
        UserRepositoryFactory.getInstance().create().setActive(
                false, this.selectedUsers.stream().map(
                        c -> {
                            int idx = this.userTable.getItems().indexOf(c);
                            c.setActive(true);
                            this.userTable.getItems().set(idx, c);
                            return c.getId();
                        }
                ).toList()
        );
    }

    @FXML
    private void onUserAllow() {
        UserRepositoryFactory.getInstance().create().setActive(
                true, this.selectedUsers.stream().map(
                        c -> {
                            int idx = this.userTable.getItems().indexOf(c);
                            c.setActive(true);
                            this.userTable.getItems().set(idx, c);
                            return c.getId();
                        }
                ).toList()
        );
    }

    @FXML
    private void onUserRemove() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure you want to delete selected user(s)");
        Optional<ButtonType> type = alert.showAndWait();

        type.ifPresent((b) -> {
            if (b.equals(ButtonType.OK)) {
                UserRepositoryFactory.getInstance().create().deleteUser(
                        this.selectedUsers.stream().map(DisplayableUser::getId).toList()
                );

                this.selectedUsers.forEach(
                        v -> this.userTable.getItems().remove(v)
                );
                this.selectedUsers.clear();
            }
        });
    }

    @FXML
    private void onUserAdd() {
        this.switchToScene(SceneType.ADMIN_USER_ADD);
    }
}
