package me.justapie.majutsu.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.*;

import me.justapie.majutsu.db.repository.user.UserRepository;
import me.justapie.majutsu.db.repository.user.UserRepositoryFactory;
import me.justapie.majutsu.utils.CryptoUtils;

public class UIMainManagement extends Application {

    private Scene loginScene;

    @Override
    public void start(Stage primaryStage) {
        Stage window = primaryStage;

        // email field.
        Label emailLabel = new Label("Email:");
        TextField emailTextfield = new TextField();

        // password field.
        Label passwordLabel = new Label("Password:");
        PasswordField passwordTextfield = new PasswordField();

        // Login procedure.
        Button loginButton = new Button("Login.");
        Label messageLabel = new Label();
        loginButton.setOnAction(e -> {
            String currentEmail = emailTextfield.getText();
            String currentPassword = passwordTextfield.getText();

            UserRepository userPool = UserRepositoryFactory.getInstance().create();

            String hashedPassword = userPool.getPassword(currentEmail);
            if (CryptoUtils.getInstance().comparePassword(currentPassword, hashedPassword)) {
                messageLabel.setText("Login sucessful!");
            }
            else {
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

            GridPane.setHalignment(passwordTextfield, HPos.LEFT);
            root.add(passwordTextfield, 1, 2, 1, 1);

            GridPane.setHalignment(loginButton, HPos.RIGHT);
            root.add(loginButton, 1, 3, 1, 1);

            loginScene = new Scene(root, 600, 300);
            window.setTitle("Welcome to ... dmm tu dien ten app");
            window.setScene(loginScene);
            window.show();
        }
    }
}
