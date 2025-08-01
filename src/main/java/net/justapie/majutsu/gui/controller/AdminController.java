package net.justapie.majutsu.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import net.justapie.majutsu.db.schema.user.User;
import net.justapie.majutsu.db.schema.user.UserRole;
import net.justapie.majutsu.gui.SessionStore;

import javax.swing.text.TableView;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class AdminController extends BaseController implements Initializable {
    @FXML
    private TableView bookTable;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User user = SessionStore.getInstance().getCurrentUser();

        if (Objects.isNull(user) || !user.getRole().equals(UserRole.ADMIN)) {

            return;
        }


    }
}
