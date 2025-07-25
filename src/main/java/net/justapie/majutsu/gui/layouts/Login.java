package net.justapie.majutsu.gui.layouts;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import net.justapie.majutsu.db.repository.user.UserRepository;
import net.justapie.majutsu.db.repository.user.UserRepositoryFactory;
import net.justapie.majutsu.gui.Composition;

public class Login extends Composition<GridPane> {
    public Login() {
        super(new GridPane());
    }

    @Override
    public void setup() {
        Label emailLabel = new Label("Email:");
        TextField emailTextfield = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordTextField = new PasswordField();

        Button loginButton = getLoginButton(emailTextfield, passwordTextField);

        {
            // Root configuration.
            this.getLayout().setPadding(new Insets(20));
            this.getLayout().setHgap(15);
            this.getLayout().setVgap(10);

            // Just some stoopid thing you should always say.
            this.getLayout().add(new Label("Enter email and password!"), 0, 0, 2, 1);

            // Email field:
            {
                // Email label.
                GridPane.setHalignment(emailLabel, HPos.RIGHT);
                this.getLayout().add(emailLabel, 0, 1, 1, 1);

                // Email textfield.
                GridPane.setHalignment(emailTextfield, HPos.LEFT);
                this.getLayout().add(emailTextfield, 1, 1, 1, 1);
            }

            // Password field:
            {
                // Password label.
                GridPane.setHalignment(passwordLabel, HPos.RIGHT);
                this.getLayout().add(passwordLabel, 0, 2, 1, 1);

                // Password textfield.
                GridPane.setHalignment(passwordTextField, HPos.LEFT);
                this.getLayout().add(passwordTextField, 1, 2, 1, 1);
            }

            // Login button.
            GridPane.setHalignment(loginButton, HPos.RIGHT);
            this.getLayout().add(loginButton, 1, 3, 1, 1);
        }
    }

    private Button getLoginButton(TextField emailTextfield, PasswordField passwordTextField) {
        Button loginButton = new Button("Login.");
        loginButton.setOnAction(e -> {
            String currentEmail = emailTextfield.getText();
            String currentPassword = passwordTextField.getText();

            // fill this. Remember username.
            UserRepository userPool = UserRepositoryFactory.getInstance().create();

//            String hashedPassword = userPool.getPassword(currentEmail);
//            if (CryptoUtils.getInstance().comparePassword(currentPassword, hashedPassword)) {
//                associatedComponent.getManagerI().run();
//            } else {
//                emailTextfield.clear();
//                passwordTextField.clear();
//            }
        });
        return loginButton;
    }
}
