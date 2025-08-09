package net.justapie.majutsu.db.repository.book;

import ch.qos.logback.classic.Logger;
import net.justapie.majutsu.cache.Cache;
import net.justapie.majutsu.db.DbClient;
import net.justapie.majutsu.db.schema.book.Book;
import net.justapie.majutsu.gui.model.DisplayableBook;
import net.justapie.majutsu.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookRepository {
    private static final Logger LOGGER = Utils.getInstance().getRootLogger().getLoggerContext().getLogger(BookRepository.class);
    private static final Connection CONNECTION = DbClient.getInstance().getConnection();

    BookRepository() {
    }

    public List<Book> getAllBooks() {
        LOGGER.debug("Preparing fetch all books in db");
        try {
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
            LOGGER.error("Failed while fetching all books");
        }
        return Collections.emptyList();
    }

    public void addBook(List<DisplayableBook> book) {
        LOGGER.debug("Preparing to add books to db");
        try (PreparedStatement stmt = CONNECTION.prepareStatement(
                "INSERT INTO books (id) VALUES (?);"
        )) {

            for (final Book b : book) {
                stmt.setString(1, b.getId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            LOGGER.error("Failed to insert books to db");
            LOGGER.error(e.getMessage());
        }
    }

    public Book getBookById(String id) {
        LOGGER.debug("Preparing to get book {}", id);

        try (PreparedStatement stmt = CONNECTION.prepareStatement(
                "SELECT * FROM users WHERE id = ?"
        )) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            return Book.fromResultSet(rs);
        } catch (SQLException e) {
            LOGGER.error("Failed to get book {}", id);
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public void setBookAvailability(String bookId, boolean available) {
        LOGGER.debug("Setting book availability for id: {} to {}", bookId, available);
        try (PreparedStatement stmt = CONNECTION.prepareStatement(
                "UPDATE books SET available = ? WHERE id = ?;")) {
            stmt.setBoolean(1, available);
            stmt.setString(2, bookId);
            stmt.executeUpdate();

            Cache.getInstance().remove("books");
            
        } catch (SQLException e) {
            LOGGER.error("Failed to update book availability for id: {}", bookId);
            LOGGER.error(e.getMessage());
        }
    }
}
