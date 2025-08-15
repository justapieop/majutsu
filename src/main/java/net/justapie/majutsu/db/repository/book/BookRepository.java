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

        LOGGER.debug("Checking cached books {}", ids);

        List<Book> result = new ArrayList<>();
        for (int i = ids.size() - 1; i >= 0; i--) {
            String bookId = ids.get(i);
            CacheObject cacheData = Cache.getInstance().get("book:" + bookId);
            if (!Objects.isNull(cacheData) && !cacheData.isExpired()) {
                LOGGER.debug("Book: {} hit cache", bookId);
                result.add((Book) cacheData.getData());
                ids.remove(i);
            }
        }
        if (ids.isEmpty()) {
            LOGGER.debug("All books hit cache, don't need to fetch");
        }
        else {
            LOGGER.debug("Preparing to fetch missing books");
            result.addAll(forceBookFetch(ids));
        }
        return result;
    }

    private String createPlaceholder(int count) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append("?");
            if (i + 1 < count) {
                result.append(", ");
            }
        }
        return result.toString();
    }

    public List<Book> forceBookFetch(List<String> ids) {
        LOGGER.debug("Fetching books {}", ids);

        try (PreparedStatement stmt = CONNECTION.prepareStatement(
                "SELECT * FROM books WHERE id IN (" + createPlaceholder(ids.size()) + ")"
        )) {
            for (int i = 0; i < ids.size(); i++) {
                stmt.setString(i + 1, ids.get(i));
            }
            ResultSet rs = stmt.executeQuery();

            List<Book> books = new ArrayList<>();

            while (rs.next()) {
                Book currentBook = Book.fromResultSet(rs);
                assert currentBook != null;
                books.add(currentBook);
                Cache.getInstance().put("book:" + currentBook.getId(), books, Cache.INDEFINITE_TTL);
            }

            return books;
        } catch (SQLException e) {
            LOGGER.debug("Failed while fetching books {}", ids);
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
