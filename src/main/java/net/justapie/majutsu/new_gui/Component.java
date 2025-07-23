import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.*;

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
