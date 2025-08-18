package net.justapie.majutsu.gui.controller.prep;

import net.justapie.majutsu.db.repository.history.HistoryRepositoryFactory;
import net.justapie.majutsu.db.schema.book.Book;
import net.justapie.majutsu.db.schema.history.ActionType;
import net.justapie.majutsu.db.schema.history.History;
import net.justapie.majutsu.gui.SessionStore;

import java.util.List;

public class DataPreprocessing {
    static final long AVAILABLE_DURATION = 14L * 24 * 60 * 60 * 1000;
    public static BookStatusType getBookStatus(Book book) {
        String bookId = book.getId();
        long userId = SessionStore.getInstance().getCurrentUserId();

        List<History> latestHistory = HistoryRepositoryFactory.getInstance().create().getLatestRecordByBookIdAndUserId(bookId, userId);

        if (latestHistory.isEmpty()) {
            return BookStatusType.AVAILABLE;
        }
        else {
            History history = latestHistory.getFirst();
            if (history.getAction() == ActionType.RETURN) {
                return BookStatusType.AVAILABLE;
            }
            else {
                long diff = System.currentTimeMillis() - history.getCreatedAt().getTime();
                return (diff > AVAILABLE_DURATION ? BookStatusType.EXPIRED : BookStatusType.BORROWED);
            }
        }
    }

    public static boolean isExpired(Book book) {
        return getBookStatus(book).equals(BookStatusType.EXPIRED);
    }
}
