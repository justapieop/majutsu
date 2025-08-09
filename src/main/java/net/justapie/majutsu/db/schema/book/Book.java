package net.justapie.majutsu.db.schema.book;

import net.justapie.majutsu.gbook.GBookClient;
import net.justapie.majutsu.gbook.model.Volume;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Date;


public class Book extends Volume {
    protected Date createdAt;
    protected boolean available;
    protected int copies;

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

            gBook.available = resultSet.getBoolean("available");
            gBook.copies = resultSet.getInt("copies");
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

    public int getCopies() {
        return this.copies;
    }
}
