package net.justapie.majutsu.gui;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.geometry.Pos;
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
import net.justapie.majutsu.db.repository.user.UserRepository;
import net.justapie.majutsu.db.repository.user.UserRepositoryFactory;
import net.justapie.majutsu.utils.CryptoUtils;

class dashboardStage extends Stage {
    private int currentState;

    public dashboardStage() {
        currentState = 0;
    }
}

public class MainGUI {
    private Stage window;
    public void createDashboard(String title) {
        window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(600);
        window.setMinHeight(300);

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(0, 0, 0, 0));

        Image logo = new Image(""); // insert here.
    }
}
