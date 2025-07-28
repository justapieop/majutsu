package net.justapie.majutsu.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import net.justapie.majutsu.db.schema.user.User;
import net.justapie.majutsu.gui.SceneType;
import net.justapie.majutsu.gui.SessionStore;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class DashboardController extends BaseController implements Initializable {
    @FXML
    private ComboBox<String> comboBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = SessionStore.getInstance().getCurrentUser();

        if (Objects.isNull(user)) return;

        this.comboBox.setValue("Welcome! " + user.getName());

        this.comboBox.getItems().add("My account");
        this.comboBox.getItems().add("Logout");

        this.comboBox.setOnAction(event -> {
            String selectedItem = this.comboBox.getSelectionModel().getSelectedItem();

            switch (selectedItem) {
                case "My account": {
                    this.switchToScene((Node) event.getSource(), SceneType.ACCOUNT);
                    break;
                }

                case "Logout": {
                    this.switchToScene((Node) event.getSource(), SceneType.LOGIN);
                    break;
                }
            }
        });
    }

}
