package net.justapie.majutsu.db.repository.document;

import ch.qos.logback.classic.Logger;
import net.justapie.majutsu.cache.Cache;
import net.justapie.majutsu.cache.CacheObject;
import net.justapie.majutsu.db.DbClient;

import net.justapie.majutsu.db.schema.book.Book;
import net.justapie.majutsu.gbook.GBookClient;
import net.justapie.majutsu.gbook.fetcher.VolumeFetcher;
import net.justapie.majutsu.gbook.model.Volume;
import net.justapie.majutsu.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DocumentRepository {
    private static final Logger LOGGER = Utils.getInstance().getRootLogger().getLoggerContext().getLogger(DocumentRepository.class);
    private static final Connection CONNECTION = DbClient.getInstance().getConnection();

    public List<Book> getAllBooks() {
        try {
            CacheObject<ArrayList<Book>> cachedBooks = Cache.getInstance().get("books");

            if (!Objects.isNull(cachedBooks)) {
                return Collections.unmodifiableList(cachedBooks.getData());
            }

            ResultSet rs = CONNECTION.createStatement()
                    .executeQuery(
                            "SELECT * FROM books;"
                    );

            ArrayList<Book> books = new ArrayList<>();

            while (rs.next()) {
                books.add(
                        Book.fromResultSet(rs)
                );
            }

            Cache.getInstance().put("books", books);

            return Collections.unmodifiableList(books);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Book createBookById(String id){
        VolumeFetcher fetcher = GBookClient.getInstance().getVolumeById(id);
        Volume volume = fetcher.get();
        String sql = "INSERT INTO documents (id, borrowed, created_at, updated_at) VALUES (?, 0, strftime('%s', 'now'), strftime('%s', 'now'))";
        try(PreparedStatement stmt = CONNECTION.prepareStatement(sql)){
            stmt.setString(1, volume.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error("Error inserting book: " + e.getMessage());
            return null;
        }
        return Book.fromVolume(volume);
    }
  

    public boolean deleteDocument(String id){
        String sql = "DELETE FROM documents WHERE id = ?";
        try (PreparedStatement stmt = CONNECTION.prepareStatement(sql)){
            stmt.setString(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            LOGGER.error("Error deleteing document:" + e.getMessage());
            return false;
        }
    }

}
