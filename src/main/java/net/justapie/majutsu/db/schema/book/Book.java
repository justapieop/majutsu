package net.justapie.majutsu.db.schema.book;

import net.justapie.majutsu.db.DbClient;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;


public class Book implements Borrowable {
    public long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public List<String> getAuthors() {
        return authors;
    }
    public String getPublisher() {
        return publisher;
    }
    public LocalDate getPublishDate() {
        return publishDate;
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
    public DocumentStatus getStatus() {
        return status;
    }
    public String getBookType() {
        return bookType;
    }
    public String getLanguage() {
        return language;
    }
    public int getPageCount() {
        return pageCount;
    }
    final int DEFAULT_LOAN_DATES = 14; 
    
    
    private long id;
    private String title;
    private List<String> authors;
    private String publisher;
    private LocalDate publishDate;
    private String isbn;

    

    private String borrowedBy;
    private LocalDate borrowedAt;
    private LocalDate dueDate;

    private LocalDate createdAt;
    private LocalDate updatedAt;
    private DocumentStatus status;

    private String bookType;
    private String language;
    private int pageCount;



    public static Book fromResultSet(ResultSet rs) {
        final Book book = new Book();
        try {
            book.id = rs.getLong("id");
            book.title = rs.getString("title");
            book.authors = List.of(rs.getString("authors").split(","));
            book.publisher = rs.getString("publisher");
            book.publishDate = rs.getDate("publish_date").toLocalDate();
            book.isbn = rs.getString("isbn");
            book.bookType = rs.getString("book_type");
            book.language = rs.getString("language");
            book.pageCount = rs.getInt("page_count");
            book.borrowedBy = rs.getString("borrowed_by");
            book.borrowedAt = rs.getDate("borrowed_at").toLocalDate();
            book.dueDate = rs.getDate("due_date").toLocalDate();
            book.createdAt = rs.getDate("created_at").toLocalDate();
            book.updatedAt = rs.getDate("updated_at").toLocalDate();
            book.status = DocumentStatus.valueOf(rs.getString("status"));
        } catch (SQLException e) {
            return null;
        }
        return book;
    }

    @Override
    public boolean borrow(String user) {
        if (this.status == DocumentStatus.BORROWED) {
            return false;
        }
        this.borrowedBy = user;
        this.borrowedAt = LocalDate.now();
        this.dueDate = LocalDate.now().plusDays(DEFAULT_LOAN_DATES); // Example: 2 weeks loan
        this.status = DocumentStatus.BORROWED;
        return true;
    }
    @Override

   
}
