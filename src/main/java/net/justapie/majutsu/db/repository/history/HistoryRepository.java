package net.justapie.majutsu.db.repository.history;

import ch.qos.logback.classic.Logger;
import net.justapie.majutsu.db.DbClient;
import net.justapie.majutsu.db.schema.history.ActionType;
import net.justapie.majutsu.db.schema.history.History;
import net.justapie.majutsu.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryRepository {
    private static final Logger LOGGER = Utils.getInstance().getRootLogger().getLoggerContext().getLogger(HistoryRepository.class);
    private static final Connection CONNECTION = DbClient.getInstance().getConnection();

    HistoryRepository() {
    }

    public List<History> getLatestRecords() {
        LOGGER.debug("Getting latest record for all books");
        try {
            PreparedStatement stmt = CONNECTION.prepareStatement(
                    "SELECT DISTINCT history.book_id FROM history;"
            );
            ResultSet rs = stmt.executeQuery();
            ArrayList<String> ids = new ArrayList<>();

            while (rs.next()) {
                ids.add(rs.getString("book_id"));
            }

            stmt = CONNECTION.prepareStatement(
                    "SELECT * FROM history WHERE id = ? ORDER BY created_at DESC LIMIT 1"
            );

            ArrayList<History> histories = new ArrayList<>();

            for (final String id : ids) {
                stmt.setString(1, id);
                rs = stmt.executeQuery();
                histories.add(History.fromResultSet(rs));
            }

            return Collections.unmodifiableList(histories);
        } catch (SQLException e) {
            LOGGER.error("Failed to get latest record for all books");
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public List<History> getLatestRecordByBookId(String bookId) {
        LOGGER.debug("Getting latest record for book {}", bookId);
        try (PreparedStatement stmt = CONNECTION.prepareStatement(
                "SELECT * FROM history WHERE book_id = ? ORDER BY created_at DESC LIMIT 1"
        )) {
            stmt.setString(1, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.isBeforeFirst()) {
                return null;
            }

            List<History> histories = new ArrayList<>();

            while (rs.next()) {
                histories.add(
                        History.fromResultSet(rs)
                );
            }

            return histories;
        } catch (SQLException e) {
            LOGGER.error("Failed to get latest record for book {}", bookId);
            LOGGER.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<History> getLatestRecordByBookIdAndUserId(String bookId, long userId) {
        LOGGER.debug("Getting latest record for book {}", bookId);
        try (PreparedStatement stmt = CONNECTION.prepareStatement(
                "SELECT * FROM history WHERE book_id = ? AND user_id = ? ORDER BY created_at DESC LIMIT 1"
        )) {
            stmt.setString(1, bookId);
            stmt.setLong(2, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.isBeforeFirst()) {
                return null;
            }

            List<History> histories = new ArrayList<>();

            while (rs.next()) {
                histories.add(
                        History.fromResultSet(rs)
                );
            }

            return histories;
        } catch (SQLException e) {
            LOGGER.error("Failed to get latest record for book {}", bookId);
            LOGGER.error(e.getMessage());
            return Collections.emptyList();
        }
    }

    public History getLatestRecordByUserId(long userId) {
        LOGGER.debug("Getting latest record of user {}", userId);
        try (PreparedStatement stmt = CONNECTION.prepareStatement(
                "SELECT * FROM history WHERE id = ? ORDER BY created_at DESC LIMIT 1"
        )) {
            stmt.setLong(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.isBeforeFirst()) {
                return null;
            }

            return History.fromResultSet(rs);
        } catch (SQLException e) {
            LOGGER.error("Failed to get latest record for user {}", userId);
            LOGGER.error(e.getMessage());
            return null;
        }
    }

    public void recordBorrow(long userId, String bookId) {
        LOGGER.debug("Recording borrow action for user: {} and book: {}", userId, bookId);
        recordAction(userId, bookId, ActionType.BORROW);
    }

    public void recordReturn(long userId, String bookId) {
        LOGGER.debug("Recording return action for user: {} and book: {}", userId, bookId);
        recordAction(userId, bookId, ActionType.RETURN);
    }

    private void recordAction(long userId, String bookId, ActionType action) {
        try (PreparedStatement stmt = CONNECTION.prepareStatement(
                "INSERT INTO history (id, user_id, book_id, action) VALUES (?, ?, ?, ?);")) {
            stmt.setLong(1, Utils.getInstance().generateSnowflakeId());
            stmt.setLong(2, userId);
            stmt.setString(3, bookId);
            stmt.setString(4, action.toString());
            stmt.executeUpdate();
            
            LOGGER.debug("Successfully recorded {} action for user: {} and book: {}", action, userId, bookId);
            
        } catch (SQLException e) {
            LOGGER.error("Failed to record {} action for user: {} and book: {}", action, userId, bookId);
            LOGGER.error(e.getMessage());
        }
    }
}
