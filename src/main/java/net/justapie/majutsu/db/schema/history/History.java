package net.justapie.majutsu.db.schema.history;

import net.justapie.majutsu.db.repository.book.BookRepositoryFactory;
import net.justapie.majutsu.db.repository.user.UserRepositoryFactory;
import net.justapie.majutsu.db.schema.book.Book;
import net.justapie.majutsu.db.schema.user.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;

public class History {
    private long id;
    private User user;
    private Book book;
    private ActionType action;
    private Date createdAt;

    public static History fromResultSet(ResultSet rs) {
        History history = new History();

        try {
            history.id = rs.getLong("id");
            history.user = UserRepositoryFactory.getInstance().create().getUserById(
                    rs.getLong("user_id")
            );
            history.book = BookRepositoryFactory.getInstance().create().getBookById(
                    rs.getString("book_id")
            );
            history.action = ActionType.valueOf(rs.getString("action"));
            history.createdAt = Date.from(Instant.ofEpochSecond(rs.getLong("created_at")));
        } catch (SQLException e) {
            return null;
        }

        return history;
    }

    public long getId() {
        return this.id;
    }

    public User getUser() {
        return this.user;
    }

    public Book getBook() {
        return this.book;
    }

    public ActionType getAction() {
        return this.action;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }
}
