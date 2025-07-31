package net.justapie.majutsu.db.repository.document;

import ch.qos.logback.classic.Logger;
import net.justapie.majutsu.db.DbClient;

import net.justapie.majutsu.db.schema.book.Book;
import net.justapie.majutsu.gbook.GBookClient;
import net.justapie.majutsu.gbook.fetcher.SearchVolumeFetcher;
import net.justapie.majutsu.gbook.fetcher.VolumeFetcher;
import net.justapie.majutsu.gbook.model.Volume;
import net.justapie.majutsu.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDate;


public class DocumentRepository {
    private static final Logger LOGGER = Utils.getInstance().getRootLogger().getLoggerContext().getLogger(DocumentRepository.class);
    private static final Connection CONNECTION = DbClient.getInstance().getConnection();
    
    private boolean updateTime(String id){
        String sql = "UPDATE documents SET updated_at = strftime('%s', 'now') WHERE id = ?";
        try(PreparedStatement stmt = CONNECTION.prepareStatement(sql)){
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e){
            LOGGER.error("Error updating timestamp: " + e.getMessage());
            return false;
        }
    }

    public Book getDocumentById(String id){
        String sql = "SELECT * FROM documents WHERE id = ?";
        try(PreparedStatement stmt = CONNECTION.prepareStatement(sql)){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return Book.fromResultSet(rs);
            }
        } catch (SQLException e){
                LOGGER.error("Error fetching document by id: " + e.getMessage());
            }
        return null;

    }

    public ArrayList<Book> searchDocumentsByTitle(String title){
        ArrayList<Book> results = new ArrayList<>();
        String sql = "SELECT * FROM documents WHERE title LIKE ?";
        try (PreparedStatement stmt = CONNECTION.prepareStatement(sql)){
            stmt.setString(1, sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = Book.fromResultSet(rs);
                if(book != null) results.add(book);
            }
        } catch (SQLException e) {
            LOGGER.error("Error searching documents by title: " + e.getMessage());
        }
    }


    public boolean setAvailable(String id) {
        String sql = "UPDATE documents SET borrowed = 0, borrowed_by = NULL, borrowed_at = NULL, due_date = NULL WHERE id = ?";
        try (PreparedStatement stmt = CONNECTION.prepareStatement(sql)) {
            stmt.setString(1, id);
            updateTime(id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error resetting borrow status: " + e.getMessage());
            return false;
        }
    }

    public boolean setBorrowed(String id, String borrowName, LocalDate returnDate) {
        String sql = "UPDATE documents SET borrowed = 1, borrowed_by = ?, borrowed_at = ?, due_date = ? WHERE id = ?";
        LocalDate now = LocalDate.now();
        try (PreparedStatement stmt = CONNECTION.prepareStatement(sql)) {
            stmt.setString(1, borrowName);
            stmt.setObject(2, now);
            stmt.setObject(3, returnDate);
            stmt.setString(4, id); 
            updateTime(id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error updating borrowed status: " + e.getMessage());
            return false;
        }
    }

    public Book createBookById(String id){
        VolumeFetcher fetcher = GBookClient.getInstance().getVolumeById(id);
        fetcher.run();
        Volume volume = fetcher.get();
        String sql = "INSERT INTO documents (id, borrowed_by, borrowed_at, return_date, borrowed) VALUES (?, null, null, null, 0)";
        try(PreparedStatement stmt = CONNECTION.prepareStatement(sql)){
            stmt.setString(1, volume.getId());
        }
    }
  

    public boolean deleteDocument(String id){
        String sql = "DELETE FROM documents WHERE id = ?";
        try (PreparedStatement stmt = CONNECTION.prepareStatement(sql)){
            stmt.setString(1, id);
            updateTime(id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            LOGGER.error("Error deleteing document:" + e.getMessage());
            return false;
        }
    }

}
