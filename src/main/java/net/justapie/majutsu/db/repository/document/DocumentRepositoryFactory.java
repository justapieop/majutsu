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

    @Override
    public DocumentRepository create() {
        Connection connection = DbClient.getInstance().getConnection();

        try {
            LOGGER.info("Getting document repository. Creating repository if not exists");
            connection.createStatement().execute(
                    "CREATE TABLE IF NOT EXISTS documents (" +
                            "id tEXT PRIMARY KEY NOT NULL UNIQUE," +
    
                            "borrowed_by LONG," +
                            "borrowed_at DATE," +
                            "return_date DATE," +
                            "borrowed BOOLEAN NOT NULL,"+
                        
                      
                            "created_at INTEGER DEFAULT (strftime('%s', 'now')),"+
                            "updated at INTEGER DEFAULT (strftime('%s', 'now'))"+
                            ");"
            );

        
            
            LOGGER.info("Document repository created");

            LOGGER.info("Creating index for documents if not exists");
            connection.createStatement().execute("CREATE UNIQUE INDEX IF NOT EXISTS documents_idx ON documents (" +
                    "id" + ");");
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
