package net.justapie.majutsu.db.schema.book;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class Paper implements Borrowable {
    final int DEFAULT_PAPER_LOAN_DATES = 7; 
    
    private long id;
    private String title;
    private List<String> authors;
    private String publisher;
    private LocalDate publishDate;
    private String isbn;
    private String language;
    private int pageCount;

    private String borrowedBy;
    private LocalDate borrowedAt;
    private LocalDate dueDate;

    private LocalDate createdAt;
    private LocalDate updatedAt;
    private DocumentStatus status;
    
    private String conference;
    private String journal;
    private String volume;
    private String issue;
    private String doi;
    private Long sourceBookId;
    private String documentType;

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

    public String getLanguage() {
        return language;
    }

    public int getPageCount() {
        return pageCount;
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

    public String getConference() {
        return conference;
    }

    public String getJournal() {
        return journal;
    }

    public String getVolume() {
        return volume;
    }

    public String getIssue() {
        return issue;
    }

    public String getDoi() {
        return doi;
    }

    public Long getSourceBookId() {
        return sourceBookId;
    }

    public String getDocumentType() {
        return documentType;
    }


    public static Paper fromResultSet(ResultSet rs) {
        final Paper paper = new Paper();
        try {
            paper.id = rs.getLong("id");
            paper.title = rs.getString("title");
            paper.authors = List.of(rs.getString("authors").split(","));
            paper.publisher = rs.getString("publisher");
            paper.publishDate = rs.getDate("publish_date").toLocalDate();
            paper.isbn = rs.getString("isbn");
            paper.language = rs.getString("language");
            paper.pageCount = rs.getInt("page_count");
            paper.conference = rs.getString("conference");
            paper.journal = rs.getString("journal");
            paper.volume = rs.getString("volume");
            paper.issue = rs.getString("issue");
            paper.doi = rs.getString("doi");
            paper.sourceBookId = rs.getLong("source_book_id");
            paper.borrowedBy = rs.getString("borrowed_by");
            paper.borrowedAt = rs.getDate("borrowed_at").toLocalDate();
            paper.dueDate = rs.getDate("due_date").toLocalDate();
            paper.createdAt = rs.getDate("created_at").toLocalDate();
            paper.updatedAt = rs.getDate("updated_at").toLocalDate();
            paper.status = DocumentStatus.valueOf(rs.getString("status"));
        } catch (SQLException e) {
            return null;
        }
        return paper;
    }

    @Override
    public boolean borrow(String user) {
        if (this.status == DocumentStatus.BORROWED) {
            return false;
        }
        this.borrowedBy = user;
        this.borrowedAt = LocalDate.now();
        this.dueDate = LocalDate.now().plusDays(DEFAULT_PAPER_LOAN_DATES); // Example: 2 weeks loan
        this.status = DocumentStatus.BORROWED;
        return true;
    }

    @Override
    public boolean borrow(String user, int days) {
        if (this.status == DocumentStatus.BORROWED) {
            return false;
        }
        this.borrowedBy = user;
        this.borrowedAt = LocalDate.now();
        this.dueDate = LocalDate.now().plusDays(days); 
        this.status = DocumentStatus.BORROWED;
        return true;
    }
    @Override
    public void returnItem()
    {
         this.status = DocumentStatus.AVAILABLE;
    }
    @Override
    public boolean isAvailable(){
        return this.status == DocumentStatus.AVAILABLE;
    }
    @Override
    public boolean isBorrowed(){
        return this.status == DocumentStatus.BORROWED;
    }



}
