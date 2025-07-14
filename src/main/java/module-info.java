module me.justapie.majutsu {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;

    opens me.justapie.majutsu to javafx.fxml;
    exports me.justapie.majutsu;
}