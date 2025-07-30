package net.justapie.majutsu.db.schema.book;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.sql.ResultSet;


public class Book {    
    private long id;
    private String title;
    private List<String> authors;
    private String isbn;

    

    private String borrowedBy;
    private LocalDate borrowedAt;
    private LocalDate dueDate;
    private boolean Borrowed;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getBorrowedBy() {
        return borrowedBy;
    }

    public LocalDate getBorrowedAt() {
        return borrowedAt;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public static Book fromResultSet(ResultSet rs) {
        final Book book = new Book();
        try {
            book.id = rs.getLong("id");
            book.title = rs.getString("title");
            book.authors = List.of(rs.getString("authors").split(","));
            book.isbn = rs.getString("isbn");
            book.borrowedBy = rs.getString("borrowed_by");
            book.borrowedAt = rs.getDate("borrowed_at").toLocalDate();
            book.dueDate = rs.getDate("due_date").toLocalDate();
            book.createdAt = rs.getDate("created_at").toLocalDate();
            book.updatedAt = rs.getDate("updated_at").toLocalDate();
            book.Borrowed = rs.getBoolean("borrowed");
        } catch (SQLException e) {
            return null;
        }
        return book;
    }

   
}
