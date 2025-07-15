module me.justapie.majutsu {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires io.github.cdimascio.dotenv.java;
    requires java.sql;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires de.mkammerer.argon2.nolibs;
    requires snowflake.lib;
    requires org.xerial.sqlitejdbc;

    opens me.justapie.majutsu to javafx.fxml;
    exports me.justapie.majutsu;
}