package net.justapie.majutsu.gui;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import net.justapie.majutsu.db.repository.user.UserRepository;
import net.justapie.majutsu.db.repository.user.UserRepositoryFactory;
import net.justapie.majutsu.utils.CryptoUtils;

public class UIManager extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        Label emailLabel = new Label("Email:");
        TextField emailTextfield = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordTextField = new PasswordField();

        Button loginButton = new Button("Login.");

        Label messageLabel = new Label();
        loginButton.setOnAction(e -> {
            String currentEmail = emailTextfield.getText();
            String currentPassword = passwordTextField.getText();

            UserRepository userPool = UserRepositoryFactory.getInstance().create();

            String hashedPassword = userPool.getPassword(currentEmail);
            if (CryptoUtils.getInstance().comparePassword(currentPassword, hashedPassword)) {
                messageLabel.setText("Login sucessful!");
            } else {
                messageLabel.setText("Login unsuccessful!");
            }
        });

        GridPane root = new GridPane();
        {
            root.setPadding(new Insets(20));
            root.setHgap(15);
            root.setVgap(10);

            root.add(new Label("Enter you email and password!"), 0, 0, 2, 1);

            GridPane.setHalignment(emailLabel, HPos.RIGHT);
            root.add(emailLabel, 0, 1, 1, 1);

            GridPane.setHalignment(emailTextfield, HPos.LEFT);
            root.add(emailTextfield, 1, 1, 1, 1);

            GridPane.setHalignment(passwordLabel, HPos.RIGHT);
            root.add(passwordLabel, 0, 2, 1, 1);

            GridPane.setHalignment(passwordTextField, HPos.LEFT);
            root.add(passwordTextField, 1, 2, 1, 1);

            GridPane.setHalignment(loginButton, HPos.RIGHT);
            root.add(loginButton, 1, 3, 1, 1);

            Scene loginScene = new Scene(root, 600, 300);
            primaryStage.setTitle("Welcome to ... dmm tu dien ten app");
            primaryStage.setScene(loginScene);
            primaryStage.show();
        }
    }
}
