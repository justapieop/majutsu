package net.justapie.majutsu.db.repository.document;

import ch.qos.logback.classic.Logger;
import net.justapie.majutsu.db.DbClient;
import net.justapie.majutsu.provider.RepositoryFactoryProvider;
import net.justapie.majutsu.utils.Utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;


public class BookRepositoryFactory extends RepositoryFactoryProvider<BookRepository> {
    private static final Logger LOGGER = Utils.getInstance().getRootLogger().getLoggerContext().getLogger(BookRepositoryFactory.class);
    private static final BookRepositoryFactory INSTANCE = new BookRepositoryFactory();

    @Override
    public BookRepository create() {
        final Connection connection = DbClient.getInstance().getConnection();
        LOGGER.debug("Preparing to create books table if not exists");

        try {
            connection.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS books (" +
                            "id TEXT PRIMARY KEY NOT NULL UNIQUE," +
                            "borrowed_by INTEGER," +
                            "borrowed_at INTEGER," +
                            "expected_return INTEGER," +
                            "returned_at INTEGER," +
                            "borrowed BOOLEAN NOT NULL,"+
                            "created_at INTEGER DEFAULT (strftime('%s', 'now'))," +
                            "available BOOLEAN NOT NULL DEFAULT true"+
                            ");"
            );

            connection.createStatement().execute("CREATE UNIQUE INDEX IF NOT EXISTS book_idx ON books (id);");

        } catch (SQLException e) {
            LOGGER.error("Failed while getting document repository");
            LOGGER.error(e.getMessage());
        }

        if (Objects.isNull(this.repository)) {
            this.repository = new BookRepository();
        }
        return this.repository;
    }

    public static BookRepositoryFactory getInstance() {
        return INSTANCE;
    }
}
