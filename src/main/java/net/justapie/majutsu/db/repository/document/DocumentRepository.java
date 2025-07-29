package net.justapie.majutsu.db.repository.document;

import ch.qos.logback.classic.Logger;
import net.justapie.majutsu.db.DbClient;

import net.justapie.majutsu.db.schema.book.Book;
import net.justapie.majutsu.db.schema.book.Paper;

import net.justapie.majutsu.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DocumentRepository {
    private static final Logger LOGGER = Utils.getInstance().getRootLogger().getLoggerContext().getLogger(DocumentRepository.class);
    private static final Connection CONNECTION = DbClient.getInstance().getConnection();

    public ArrayList<Object> getAllDocuments() {
        ArrayList<Object> documents = new ArrayList<>();
        String sql = "SELECT * FROM documents";
        try (PreparedStatement stmt = CONNECTION.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String type = rs.getString("document_type");
                if ("BOOK".equalsIgnoreCase(type)) {
                    Book book = Book.fromResultSet(rs);
                    if (book != null) documents.add(book);
                } else if ("PAPER".equalsIgnoreCase(type)) {
                    Paper paper = Paper.fromResultSet(rs);
                    if (paper != null) documents.add(paper);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching documents: " + e.getMessage());
        }
        return documents;
    }

    public ArrayList<Paper> getAllPapers() {
        ArrayList<Paper> papers = new ArrayList<>();
        String sql = "SELECT * FROM documents WHERE document_type = 'PAPER'";
        try (PreparedStatement stmt = CONNECTION.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Paper paper = Paper.fromResultSet(rs);
                if (paper != null) {
                    papers.add(paper);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching papers: " + e.getMessage());
        }
        return papers;
    }

    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM documents WHERE document_type = 'BOOK'";
        try (PreparedStatement stmt = CONNECTION.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = Book.fromResultSet(rs);
                if (book != null) {
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error fetching books: " + e.getMessage());
        }
        return books;
    }

  
    public boolean createBook(Book book) {
        String sql = "INSERT INTO documents (title, authors, publisher, publish_date, isbn, book_type, language, page_count, borrowed_by, borrowed_at, due_date, created_at, updated_at, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = CONNECTION.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, String.join(",", book.getAuthors()));
            stmt.setString(3, book.getPublisher());
            stmt.setObject(4, book.getPublishDate());
            stmt.setString(5, book.getIsbn());
            stmt.setString(6, book.getBookType());
            stmt.setString(7, book.getLanguage());
            stmt.setInt(8, book.getPageCount());
            stmt.setString(9, book.getBorrowedBy());
            stmt.setObject(10, book.getBorrowedAt());
            stmt.setObject(11, book.getDueDate());
            stmt.setObject(12, book.getCreatedAt());
            stmt.setObject(13, book.getUpdatedAt());
            stmt.setString(14, book.getStatus() != null ? book.getStatus().name() : null);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.error("Error creating book: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteDocument(int id){
        String sql = "DELETE FROM documents WHERE id = ?";
        try (PreparedStatement stmt = CONNECTION.prepareStatement(sql)){
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (Exception e) {
            LOGGER.error("Error deleteing document:" + e.getMessage());
            return false;
        }
    }

}
