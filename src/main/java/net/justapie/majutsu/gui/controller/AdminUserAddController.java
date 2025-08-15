package net.justapie.majutsu.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import net.justapie.majutsu.db.repository.user.UserRepositoryFactory;
import net.justapie.majutsu.db.schema.user.User;
import net.justapie.majutsu.gui.SceneType;
import net.justapie.majutsu.utils.CryptoUtils;
import net.justapie.majutsu.utils.Utils;
import net.synedra.validatorfx.Validator;

import java.util.Objects;
import java.util.Optional;

public class AdminUserAddController extends BaseController {
    @FXML
    private Label prompt;

    @FXML
    private TextField nameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField passwordField;

    @FXML
    private void onConfirm() {
        if (this.validateInput()) {
            this.prompt.setText("Please enter a proper email");
            return;
        }

        String email = this.emailField.getText();
        if (UserRepositoryFactory.getInstance().create().emailExists(email)) {
            this.prompt.setText("User already exists");
            return;
        }

        String pwd = this.passwordField.getText();
        boolean emptyPassword = pwd.isEmpty();

        if (emptyPassword) {
            pwd = CryptoUtils.getInstance().generatePassword(32);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText(
                    "Your new password will be " + pwd +
                            "\nPress cancel to change this or confirm to confirm creating an user with this password\n" +
                            "If you proceed, the password will be copied into your clipboard"
            );
            Optional<ButtonType> type = alert.showAndWait();

            final String finalPwd = pwd;
            type.ifPresent((b) -> {
                if (b.equals(ButtonType.OK)) {
                    this.createUserAndReturn(finalPwd);
                } else {
                    alert.close();
                }
            });

            return;
        }

        this.createUserAndReturn(pwd);
    }

    @FXML
    private void onBack() {
        this.switchToScene(SceneType.ADMIN);
    }

    private void createUserAndReturn(String password) {
        User user = UserRepositoryFactory.getInstance().create()
                .createUser(
                        this.nameField.getText(),
                        this.emailField.getText(),
                        password,
                        true
                );
        if (Objects.isNull(user)) {
            Utils.getInstance().displayAlert("User already exists", Alert.AlertType.ERROR);
            return;
        }
        Utils.getInstance().displayAlert("User created successfully", Alert.AlertType.INFORMATION);
        this.switchToScene(SceneType.ADMIN);
    }

    private boolean validateInput() {
        Validator validator = new Validator();
        validator.createCheck()
                .dependsOn("emailField", this.emailField.textProperty())

                .withMethod(c -> {
                    String email = c.get("emailField");

                    if (email.isEmpty() || !Utils.getInstance().checkValidEmail(email)) {
                        c.error("Invalid email");
                    }
                });

        return !validator.validate();
    }
}
