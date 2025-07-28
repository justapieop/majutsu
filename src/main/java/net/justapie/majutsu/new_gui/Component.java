package net.justapie.majutsu.new_gui;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Component {

    // Object states, do not move or change. If you do, you are gay!
    private Scene scene;
    private Runnable managerI;

    protected void setInternalAPI(Runnable internalAPI) {
        managerI = internalAPI;
    }

    protected void setScene(Scene scene) {
        this.scene = scene;
    }

    protected Runnable getManagerI() {
        return managerI;
    }

    protected void show(Stage stage) {
        stage.setScene(this.scene);
        stage.show();
    }
}
