import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.*;

import java.util.ArrayList;

public class LoginConsole {

    private Component associatedComponent;

    private String username;

    protected LoginConsole(Component component) {
        this.associatedComponent = component;
    }

    protected String getUsername() {
        return username;
    }

    protected void initializeLoginGUI(Runnable internalAPI) {

        // Set internal API.
        associatedComponent.setInternalAPI(internalAPI);

        // Email and textfield for email.
        Label emailLabel = new Label("Email:");
        TextField emailTextfield = new TextField();

        // Password and password field.
        Label passwordLabel = new Label("Password:");
        PasswordField passwordTextField = new PasswordField();

        // Login button.
        Button loginButton = new Button("Login.");
        loginButton.setOnAction(e -> {
            String currentEmail = emailTextfield.getText();
            String currentPassword = passwordTextField.getText();

            // fill this. Remember username.
            associatedComponent.getManagerI().run();
        });

        // Root initialization for Login GUI.
        GridPane root = new GridPane();
        {
            // Root configuration.
            root.setPadding(new Insets(20));
            root.setHgap(15);
            root.setVgap(10);

            // Just some stoopid thing you should always say.
            root.add(new Label("Enter email and password!"), 0, 0, 2, 1);

            // Email field:
            {
                // Email label.
                GridPane.setHalignment(emailLabel, HPos.RIGHT);
                root.add(emailLabel, 0, 1, 1, 1);

                // Email textfield.
                GridPane.setHalignment(emailTextfield, HPos.LEFT);
                root.add(emailTextfield, 1, 1, 1, 1);
            }

            // Password field:
            {
                // Password label.
                GridPane.setHalignment(passwordLabel, HPos.RIGHT);
                root.add(passwordLabel, 0, 2, 1, 1);

                // Password textfield.
                GridPane.setHalignment(passwordTextField, HPos.LEFT);
                root.add(passwordTextField, 1, 2, 1, 1);
            }

            // Login button.
            GridPane.setHalignment(loginButton, HPos.RIGHT);
            root.add(loginButton, 1, 3, 1, 1);
        }

        // Finishing procedure.
        associatedComponent.setScene(new Scene(root, 854, 480));
    }

    protected void show(Stage stage) {
        associatedComponent.show(stage);
    }
}
