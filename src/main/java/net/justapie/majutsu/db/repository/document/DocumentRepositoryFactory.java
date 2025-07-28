package net.justapie.majutsu.db.repository.document;

import ch.qos.logback.classic.Logger;
import net.justapie.majutsu.db.DbClient;
import net.justapie.majutsu.provider.RepositoryFactoryProvider;
import net.justapie.majutsu.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class DocumentRepositoryFactory extends RepositoryFactoryProvider<DocumentRepository> {
    private static final Logger LOGGER = Utils.getInstance().getRootLogger().getLoggerContext().getLogger(DocumentRepositoryFactory.class);

    @Override
    public DocumentRepository create() {
        Connection connection = DbClient.getInstance().getConnection();

        try {
            LOGGER.info("Getting user repository. Creating repository if not exists");
            PreparedStatement stmt = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS documents (" +
                            "id INTEGER PRIMARY KEY NOT NULL UNIQUE," +
                            "name TEXT NOT NULL" +
                            ");"
            );

            LOGGER.info("Document repository created");

            LOGGER.info("Creating index for document if not exists");
            connection.createStatement().execute("CREATE UNIQUE INDEX IF NOT EXISTS user_idx ON users (" +
                    "id, email" +
                    ");");
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
}
