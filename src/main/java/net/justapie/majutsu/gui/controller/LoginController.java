package net.justapie.majutsu.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import net.justapie.majutsu.db.repository.user.UserRepositoryFactory;
import net.justapie.majutsu.db.schema.user.User;
import net.justapie.majutsu.gui.SessionStore;
import net.justapie.majutsu.utils.CryptoUtils;
import net.justapie.majutsu.utils.Utils;
import net.synedra.validatorfx.Validator;

import java.util.Objects;

public class LoginController extends BaseController {
    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private void onRegisterButtonClick() {
        if (this.isWrongPassword()) {
            Utils.getInstance().displayAlert("Invalid email or password", Alert.AlertType.ERROR);
            return;
        }
        String email = this.emailField.getText();
        String password = this.passwordField.getText();

        User user = UserRepositoryFactory.getInstance().create().createUser("User", email, password, false);

        if (Objects.isNull(user)) {
            Utils.getInstance().displayAlert("User already exists", Alert.AlertType.ERROR);
            return;
        }

        SessionStore.getInstance().setCurrentUserId(
                user.getId()
        );

        Utils.getInstance().displayAlert("Registered successfully", Alert.AlertType.INFORMATION);

        new DashboardSplashController().process();
    }

    @FXML
    private void onLoginButtonClick() {
        if (this.isWrongPassword()) {
            Utils.getInstance().displayAlert("Invalid email or password", Alert.AlertType.ERROR);
            return;
        }
        String email = this.emailField.getText();
        String password = this.passwordField.getText();

        String hashedPassword = UserRepositoryFactory.getInstance().create().getPassword(email);

        if (Objects.isNull(hashedPassword) || !CryptoUtils.getInstance().comparePassword(password, hashedPassword)) {
            Utils.getInstance().displayAlert("Invalid credentials", Alert.AlertType.ERROR);
            return;
        }

        // Check if user account is blocked
        User user = UserRepositoryFactory.getInstance().create().getUserByEmail(email);
        if (Objects.isNull(user) || !user.isActive()) {
            Utils.getInstance().displayAlert("Your account has been blocked", Alert.AlertType.ERROR);
            return;
        }

        SessionStore.getInstance().setCurrentUserId(user.getId());

        Utils.getInstance().displayAlert("Logged in successfully", Alert.AlertType.INFORMATION);

        new DashboardSplashController().process();
    }

    private boolean isWrongPassword() {
        Validator validator = new Validator();
        validator.createCheck()
                .dependsOn("emailField", this.emailField.textProperty())

                .withMethod(c -> {
                    String email = c.get("emailField");

                    if (email.isEmpty() || !Utils.getInstance().checkValidEmail(email)) {
                        c.error("Invalid email");
                    }
                })
                .dependsOn("passwordField", this.passwordField.textProperty())
                .withMethod(c -> {
                    String email = c.get("passwordField");

                    if (email.isEmpty()) {
                        c.error("Invalid password");
                    }
                });

        return !validator.validate();
    }
}
