package net.justapie.majutsu.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import net.justapie.majutsu.db.repository.user.UserRepositoryFactory;
import net.justapie.majutsu.db.schema.user.User;
import net.justapie.majutsu.gui.SceneType;
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
    private Label authPrompt;


    @FXML
    private void onRegisterButtonClick() {
        if (!this.validateInput()) {
            this.authPrompt.setText("Invalid email or password");
            return;
        }
        String email = this.emailField.getText();
        String password = this.passwordField.getText();

        User user = UserRepositoryFactory.getInstance().create().createUser("User", email, password);

        if (Objects.isNull(user)) {
            this.authPrompt.setText("User already exists");
            return;
        }

        SessionStore.getInstance().setCurrentUserId(
                user.getId()
        );

        this.switchToScene(SceneType.DASHBOARD);
    }

    @FXML
    private void onLoginButtonClick() {
        if (!this.validateInput()) {
            this.authPrompt.setText("Invalid email or password");
            return;
        }
        String email = this.emailField.getText();
        String password = this.passwordField.getText();

        String hashedPassword = UserRepositoryFactory.getInstance().create().getPassword(email);

        if (Objects.isNull(hashedPassword) || !CryptoUtils.getInstance().comparePassword(password, hashedPassword)) {
            this.authPrompt.setText("Invalid credentials");
            return;
        }

        SessionStore.getInstance().setCurrentUserId(
                UserRepositoryFactory.getInstance().create().getUserIdByEmail(email)
        );

        this.switchToScene(SceneType.DASHBOARD);
    }

    private boolean validateInput() {
        Validator validator = new Validator();
        validator.createCheck()
                .dependsOn("emailField", this.emailField.textProperty())

                .withMethod(c -> {
                    String email = c.get("emailField");

                    if (email.isEmpty() || !Utils.getInstance().checkValidEmail(email)) {
                        c.error("Invalid email");
                        return;
                    }
                })
                .dependsOn("passwordField", this.passwordField.textProperty())
                .withMethod(c -> {
                    String email = c.get("passwordField");

                    if (email.isEmpty()) {
                        c.error("Invalid password");
                    }
                });

        return validator.validate();
    }
}
