package net.justapie.majutsu.db.repository.book;

import ch.qos.logback.classic.Logger;
import net.justapie.majutsu.cache.Cache;
import net.justapie.majutsu.cache.CacheObject;
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
import java.util.Objects;

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

            Cache.getInstance().put("books", books, Cache.DEFAULT_TTL);

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

    public List<Book> batchBookFetch(List<String> ids) {
        LOGGER.debug("Preparing to get books {}", ids);

        String idQuery = String.join(", ", ids);

        LOGGER.debug("Checking cached books {}", ids);
        CacheObject cachedBooks = Cache.getInstance().get("book:" + idQuery);

        if (!Objects.isNull(cachedBooks) && !cachedBooks.isExpired()) {
            LOGGER.debug("Cached books {} hit", ids);
            return (List<Book>) cachedBooks.getData();
        }

        LOGGER.debug("Cached book {} missed. Fetching", ids);

        try (PreparedStatement stmt = CONNECTION.prepareStatement(
                "SELECT * FROM books WHERE id IN (?)"
        )) {
            stmt.setString(1, idQuery);
            ResultSet rs = stmt.executeQuery();

            List<Book> books = new ArrayList<>();

            while (rs.next()) {
                books.add(Book.fromResultSet(rs));
            }

            Cache.getInstance().put("book:" + idQuery, books, Cache.INDEFINITE_TTL);

            return books;
        } catch (SQLException e) {
            LOGGER.debug("Failed while getting books {}", ids);
            LOGGER.debug(e.getMessage());
            return Collections.emptyList();
        }
    }

    public Book getBookById(String id) {
        LOGGER.debug("Preparing to get book {}", id);

        try (PreparedStatement stmt = CONNECTION.prepareStatement(
                "SELECT * FROM books WHERE id = ?"
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

    public void setBookAvailability(boolean available, List<String> bookIds) {
        LOGGER.debug("Setting book availability for id {} to {}", bookIds, available);
        try (PreparedStatement stmt = CONNECTION.prepareStatement(
                "UPDATE books SET available = ? WHERE id = ?;")) {
            for (final String id : bookIds) {
                stmt.setBoolean(1, available);
                stmt.setString(2, id);
                stmt.addBatch();
            }
            stmt.executeBatch();

            Cache.getInstance().remove("books");
        } catch (SQLException e) {
            LOGGER.error("Failed to update book availability");
            LOGGER.error(e.getMessage());
        }
    }

    public void remove(List<String> bookIds) {
        LOGGER.debug("Removing books");
        try (PreparedStatement stmt = CONNECTION.prepareStatement(
                "DELETE FROM books WHERE id = ?;"
        )) {

            for (final String id : bookIds) {
                stmt.setString(1, id);
                stmt.addBatch();
            }

            stmt.executeBatch();

            Cache.getInstance().remove("books");
        } catch (SQLException e) {
            LOGGER.error("Failed to remove books");
            LOGGER.error(e.getMessage());
        }
    }
}
