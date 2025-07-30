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
                            "id INTEGER PRIMARY KEY NOT NULL UNIQUE," +
                            "title TEXT NOT NULL," + 
                            "authors TEXT NOT NULL," + 
                            "publisher TEXT NOT NULL," + 
                            "publish_date Date NOT NULL," + 
                            "isbn TEXT NOT NULL," + 
                            "page_count INTERGER NOT NULL," +
                            
                            "borrowed_by TEXT," +
                            "borrowed_at DATE," +
                            "due_date DATE" +


                            "book_type TEXT ," +
                            "language TEXT NOT NULL," +
                            



                            "conference TEXT"+
                            "journal TEXT" +
                            "volume TEXT" +
                            "issue TEXT" +
                            "doi TEXT"+
                            


                            "source_book_id INTEGER REFERENCES documents(id) ON DELETE SET NULL," +
                            
                            "document_type TEXT NOT NULL CHECK (document_type IN ('BOOK', 'PAPER'))" +
                            //"created_at INTEGER DEFAULT (strftime('%s', 'now')),"+
                            //"updated at INTEGER DEFAULT (strftime('%s', 'now'))"+
                            ");"
            );

        
            
            LOGGER.info("Document repository created");

            LOGGER.info("Creating index for documents if not exists");
            connection.createStatement().execute("CREATE UNIQUE INDEX IF NOT EXISTS documents_idx ON documents (" +
                    "id, title, authors, publishers, isbn, page_count, book_type, language, conference, journal, volume, issue, doi, source_book_id" +
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
