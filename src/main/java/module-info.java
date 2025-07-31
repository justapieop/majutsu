module net.justapie.majutsu {
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
    requires atlantafx.base;
    requires java.net.http;
    requires com.google.gson;
    requires java.desktop;

    opens net.justapie.majutsu.gbook.model to com.google.gson;
    opens net.justapie.majutsu.gui.controller to javafx.fxml;
    exports net.justapie.majutsu.gui.controller;
    exports net.justapie.majutsu.gui;
    exports net.justapie.majutsu.gui.fetcher;
    exports net.justapie.majutsu.gui.cache;
}