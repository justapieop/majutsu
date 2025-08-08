package net.justapie.majutsu.db.repository.history;

import ch.qos.logback.classic.Logger;
import net.justapie.majutsu.db.DbClient;
import net.justapie.majutsu.utils.Utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class HistoryRepository {
    private static final Logger LOGGER = Utils.getInstance().getRootLogger().getLoggerContext().getLogger(HistoryRepository.class);
    private static final Connection CONNECTION = DbClient.getInstance().getConnection();

    HistoryRepository() {
    }

    public void recordBorrow(long userId, String bookId) {
        LOGGER.debug("Recording borrow action for user: {} and book: {}", userId, bookId);
        recordAction(userId, bookId, "BORROW");
    }

    public void recordReturn(long userId, String bookId) {
        LOGGER.debug("Recording return action for user: {} and book: {}", userId, bookId);
        recordAction(userId, bookId, "RETURN");
    }

    private void recordAction(long userId, String bookId, String action) {
        try (PreparedStatement stmt = CONNECTION.prepareStatement(
                "INSERT INTO history (user_id, book_id, action) VALUES (?, ?, ?);")) {
            stmt.setLong(1, userId);
            stmt.setString(2, bookId);
            stmt.setString(3, action);
            stmt.executeUpdate();
            
            LOGGER.debug("Successfully recorded {} action for user: {} and book: {}", action, userId, bookId);
            
        } catch (SQLException e) {
            LOGGER.error("Failed to record {} action for user: {} and book: {}", action, userId, bookId);
            LOGGER.error(e.getMessage());
        }
    }
}
