package me.justapie.majutsu.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbClient implements AutoCloseable {
    private static final DbClient INSTANCE = new DbClient();
    private Connection connection = null;

    private DbClient() {
        super();
    }

    public synchronized void connect() throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            this.connection = DriverManager.getConnection("jdbc:sqlite:data.db");
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public static DbClient getInstance() {
        return INSTANCE;
    }

    @Override
    public synchronized void close() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
            this.connection = null;
        }
    }
}
