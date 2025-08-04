package net.justapie.majutsu.db.schema.book;

import net.justapie.majutsu.db.repository.user.UserRepositoryFactory;
import net.justapie.majutsu.db.schema.user.User;
import net.justapie.majutsu.gbook.GBookClient;
import net.justapie.majutsu.gbook.model.Volume;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;


public class Book extends Volume {
    protected User borrowedBy;
    protected Date borrowedAt;
    protected Date expectedReturn;
    protected Date returnDate;
    protected Date createdAt;
    protected boolean borrowed;
    protected boolean available;

    protected Book() {
        super();
    }

    public static Book fromVolume(Volume volume) {
        Book book = new Book();

        book.id = volume.getId();
        book.volumeInfo = volume.getVolumeInfo();

        return book;
    }

    public static Book fromResultSet(ResultSet resultSet) {
        try {
            String id = resultSet.getString("id");
            Book gBook = Book.fromVolume(GBookClient.getInstance().getVolumeById(id).get());

            long borrowedUserId = resultSet.getLong("borrowed_by");

            if (borrowedUserId != 0) {
                gBook.borrowedBy = UserRepositoryFactory.getInstance().create().getUserById(borrowedUserId);
            }

            gBook.borrowedAt = Date.from(Instant.ofEpochSecond(resultSet.getLong("borrowed_at")));
            gBook.expectedReturn = Date.from(Instant.ofEpochSecond(resultSet.getLong("expected_return")));
            gBook.returnDate = Date.from(Instant.ofEpochSecond(resultSet.getLong("returned_at")));
            gBook.borrowed = resultSet.getBoolean("borrowed");
            gBook.available = resultSet.getBoolean("available");
            gBook.createdAt = Date.from(Instant.ofEpochSecond(resultSet.getLong("created_at")));

            return gBook;
        } catch (SQLException e) {
            return null;
        }
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public boolean isAvailable() {
        return this.available;
    }

    public boolean isBorrowed() {
        return this.borrowed;
    }

    public User getBorrowedBy() {
        return this.borrowedBy;
    }

    public Date getBorrowedAt() {
        return this.borrowedAt;
    }

    public Date expectedReturn() {
        return this.expectedReturn;
    }

    public Date getReturnDate() {
        return this.returnDate;
    }
}
