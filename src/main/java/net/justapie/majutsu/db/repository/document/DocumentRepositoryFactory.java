package net.justapie.majutsu.db.repository.document;

import ch.qos.logback.classic.Logger;
import net.justapie.majutsu.db.DbClient;
import net.justapie.majutsu.provider.RepositoryFactoryProvider;
import net.justapie.majutsu.utils.Utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;


public class DocumentRepositoryFactory extends RepositoryFactoryProvider<DocumentRepository> {
    private static final Logger LOGGER = Utils.getInstance().getRootLogger().getLoggerContext().getLogger(DocumentRepositoryFactory.class);
    private static final DocumentRepositoryFactory INSTANCE = new DocumentRepositoryFactory();

    @Override
    public DocumentRepository create() {
        Connection connection = DbClient.getInstance().getConnection();

        try {
            LOGGER.info("Getting document repository. Creating repository if not exists");
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

        
            
            LOGGER.info("Document repository created");

            LOGGER.info("Creating index for documents if not exists");
            connection.createStatement().execute("CREATE UNIQUE INDEX IF NOT EXISTS book_idx ON books (id);");
            LOGGER.info("Index created");
            
        } catch (SQLException e) {
            LOGGER.error("Failed while getting document repository");
            LOGGER.error(e.getMessage());
        }

        if (Objects.isNull(this.repository)) {
            this.repository = new DocumentRepository();
        }
        return this.repository;
    }

    public static DocumentRepositoryFactory getInstance() {
        return INSTANCE;
    }
}
