package net.justapie.majutsu.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import net.justapie.majutsu.db.repository.user.UserRepositoryFactory;
import net.justapie.majutsu.db.schema.user.User;
import net.justapie.majutsu.gui.SceneType;
import net.justapie.majutsu.gui.SessionStore;
import net.justapie.majutsu.utils.CryptoUtils;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AccountController extends BaseController implements Initializable {
    @FXML
    private Label changePasswordPrompt;

    @FXML
    private TextField newNameField;

    @FXML
    private TextField newPasswordField;

    @FXML
    private TextField confirmPasswordField;

    @FXML
    private void onAccountBackButton(ActionEvent event) {
        this.switchToScene((Node) event.getSource(), SceneType.DASHBOARD);
    }

    @FXML
    private void onSaveName() {
        if (this.newNameField.getText().isEmpty()) {
            this.changePasswordPrompt.setText("Please enter your new name");
            return;
        }

        User user = SessionStore.getInstance().getCurrentUser();

        if (Objects.isNull(user)) {
            return;
        }

        this.changePasswordPrompt.setText("Name changed");
        UserRepositoryFactory.getInstance().create().changeName(user.getId(), this.newNameField.getText());
    }

    @FXML
    private void onSavePassword() {
        if (this.confirmPasswordField.getText().isEmpty() || this.newPasswordField.getText().isEmpty()) {
            this.changePasswordPrompt.setText("Please enter your current and new password");
            return;
        }

        User user = SessionStore.getInstance().getCurrentUser();

        if (Objects.isNull(user)) {
            return;
        }

        if (!CryptoUtils.getInstance().comparePassword(this.confirmPasswordField.getText(), user.getHashedPassword())) {
            this.changePasswordPrompt.setText("Wrong password");
            return;
        }

        UserRepositoryFactory.getInstance().create().changePassword(user.getId(), this.newPasswordField.getText());
        this.changePasswordPrompt.setText("Password changed");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = SessionStore.getInstance().getCurrentUser();

        if (Objects.isNull(user)) return;

        this.newNameField.setText(user.getName());
    }
}
