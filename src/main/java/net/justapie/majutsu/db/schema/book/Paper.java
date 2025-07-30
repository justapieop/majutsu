package net.justapie.majutsu.db.schema.book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Paper {
    private long id;
    private String title;
    private List<String> authors;
    private String doi;

    private String borrowedBy;
    private LocalDate borrowedAt;
    private LocalDate dueDate;
    private boolean borrowed;

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

    public String getDoi() {
        return doi;
    }

    public static Paper fromResultSet(ResultSet rs) {
        final Paper paper = new Paper();
        try {
            paper.id = rs.getLong("id");
            paper.title = rs.getString("title");
            paper.authors = List.of(rs.getString("authors").split(","));
            paper.doi = rs.getString("doi");
            paper.borrowedBy = rs.getString("borrowed_by");
            paper.borrowedAt = rs.getDate("borrowed_at").toLocalDate();
            paper.dueDate = rs.getDate("due_date").toLocalDate();
            paper.createdAt = rs.getDate("created_at").toLocalDate();
            paper.updatedAt = rs.getDate("updated_at").toLocalDate();
            paper.borrowed = rs.getBoolean("borrowed");
        } catch (SQLException e) {
            return null;
        }
        return paper;
    }

   

}
