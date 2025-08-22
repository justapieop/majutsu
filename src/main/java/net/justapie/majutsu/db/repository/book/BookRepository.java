package net.justapie.majutsu.db.repository.book;

import ch.qos.logback.classic.Logger;
import net.justapie.majutsu.cache.Cache;
import net.justapie.majutsu.cache.CacheObject;
import net.justapie.majutsu.db.DbClient;
import net.justapie.majutsu.db.schema.book.Book;
import net.justapie.majutsu.gui.controller.prep.BookCacheData;
import net.justapie.majutsu.gui.controller.prep.DataPreprocessing;
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

            rs.close();

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
            
            // Invalidate relevant caches after successful book addition
            Cache.getInstance().remove("books");
            // Invalidate user caches since available books have changed
            Cache.getInstance().removeByPrefix("user:");
            
            LOGGER.debug("Successfully added {} books and invalidated caches", book.size());
        } catch (SQLException e) {
            LOGGER.error("Failed to insert books to db");
            LOGGER.error(e.getMessage());
        }
    }

    public List<Book> batchBookFetch(List<String> ids) {
        LOGGER.debug("Preparing to get books {}", ids);

        LOGGER.debug("Checking cached books {}", ids);

        List<Book> result = new ArrayList<>();
        List<String> missingIds = new ArrayList<>();
        
        for (String bookId : ids) {
            CacheObject cacheData = Cache.getInstance().get("book:" + bookId);
            if (!Objects.isNull(cacheData) && !cacheData.isExpired()) {
                LOGGER.debug("Book: {} hit cache", bookId);
                result.add(((BookCacheData) cacheData.getData()).getBook());
            } else {
                missingIds.add(bookId);
            }
        }
        
        if (missingIds.isEmpty()) {
            LOGGER.debug("All books hit cache, don't need to fetch");
        } else {
            LOGGER.debug("Preparing to fetch missing books");
            result.addAll(forceBookFetch(missingIds));
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
                BookCacheData currentBookData = new BookCacheData(currentBook, DataPreprocessing.getBookStatus(currentBook));
                Cache.getInstance().put("book:" + currentBook.getId(), currentBookData, Cache.INDEFINITE_TTL);
            }

            rs.close();

            return books;
        } catch (SQLException e) {
            LOGGER.debug("Failed while fetching books {}", ids);
            LOGGER.debug(e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Book> fetchBooksExcept(List<String> ids) {
        LOGGER.debug("Fetching books except {}", ids);

        // If no books to exclude, get all available books
        if (ids.isEmpty()) {
            try (PreparedStatement stmt = CONNECTION.prepareStatement(
                    "SELECT * FROM books WHERE available = true"
            )) {
                ResultSet rs = stmt.executeQuery();
                List<Book> books = new ArrayList<>();

                while (rs.next()) {
                    Book currentBook = Book.fromResultSet(rs);
                    assert currentBook != null;
                    books.add(currentBook);
                    BookCacheData currentBookData = new BookCacheData(currentBook, DataPreprocessing.getBookStatus(currentBook));
                    Cache.getInstance().put("book:" + currentBook.getId(), currentBookData, Cache.INDEFINITE_TTL);
                }

                rs.close();
                return books;
            } catch (SQLException e) {
                LOGGER.debug("Failed while fetching all available books");
                LOGGER.debug(e.getMessage());
                return Collections.emptyList();
            }
        }

        try (PreparedStatement stmt = CONNECTION.prepareStatement(
                "SELECT * FROM books WHERE id NOT IN (" + createPlaceholder(ids.size()) + ") AND available = true"
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
                BookCacheData currentBookData = new BookCacheData(currentBook, DataPreprocessing.getBookStatus(currentBook));
                Cache.getInstance().put("book:" + currentBook.getId(), currentBookData, Cache.INDEFINITE_TTL);
            }

            rs.close();

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
            Book book = Book.fromResultSet(rs);
            rs.close();
            return book;
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
                
                // Invalidate cache for this specific book
                Cache.getInstance().remove("book:" + id);
            }
            stmt.executeBatch();

            // Invalidate general books cache
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

            // Invalidate all relevant caches after successful book removal
            Cache.getInstance().remove("books");
            // Invalidate individual book caches for removed books
            for (String bookId : bookIds) {
                Cache.getInstance().remove("book:" + bookId);
            }
            // Invalidate user caches since available/borrowed books have changed
            Cache.getInstance().removeByPrefix("user:");
            
            LOGGER.debug("Successfully removed {} books and invalidated caches", bookIds.size());
        } catch (SQLException e) {
            LOGGER.error("Failed to remove books");
            LOGGER.error(e.getMessage());
        }
    }
}
