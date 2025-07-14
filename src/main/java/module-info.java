module me.justapie.majutsu {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires io.github.cdimascio.dotenv.java;
    requires java.sql;

    opens me.justapie.majutsu to javafx.fxml;
    exports me.justapie.majutsu;
}