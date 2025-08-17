package net.justapie.majutsu.db.repository.history;

import ch.qos.logback.classic.Logger;
import net.justapie.majutsu.db.DbClient;
import net.justapie.majutsu.provider.RepositoryFactoryProvider;
import net.justapie.majutsu.utils.Utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class HistoryRepositoryFactory extends RepositoryFactoryProvider<HistoryRepository> {
    private static final Logger LOGGER = Utils.getInstance().getRootLogger().getLoggerContext().getLogger(HistoryRepositoryFactory.class);
    private static final HistoryRepositoryFactory INSTANCE = new HistoryRepositoryFactory();

    @Override
    public HistoryRepository create() {
        final Connection connection = DbClient.getInstance().getConnection();
        LOGGER.debug("Preparing to create history table if not exists");

        try {
            connection.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS history (" +
                            "id INTEGER PRIMARY KEY UNIQUE NOT NULL," +
                            "user_id INTEGER NOT NULL REFERENCES users(id)," +
                            "book_id TEXT NOT NULL REFERENCES books(id)," +
                            "action TEXT NOT NULL CHECK (action in ('BORROW', 'RETURN'))," +
                            "created_at INTEGER NOT NULL DEFAULT (strftime('%s', 'now'))" +
                            ");"
            );

            connection.createStatement().execute("CREATE UNIQUE INDEX IF NOT EXISTS history_idx ON history(id);");
        } catch (SQLException e) {
            LOGGER.error("Failed while creating history repository");
            LOGGER.error(e.getMessage());
        }

        if (Objects.isNull(this.repository)) {
            this.repository = new HistoryRepository();
        }
        return this.repository;
    }

    public static HistoryRepositoryFactory getInstance() {
        return INSTANCE;
    }
}
