package net.justapie.majutsu.db.schema.book;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Document {
    // Basic document properties
    protected long id;
    protected String title;
    protected List<String> authors;  // Thay đổi thành List để hỗ trợ nhiều tác giả
    protected String publisher;
    protected LocalDate publishedDate;
    protected String isbn;

    // Timestamps and status
    protected LocalDate createdAt;
    protected LocalDate updatedAt;
    protected DocumentStatus status;
    protected LocalDate borrowDate;
    protected LocalDate dueDate;
    protected long borrowerId;


    protected void updateTimestamp() {
        this.updatedAt = LocalDate.now();
    }


    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
        updateTimestamp();
    }

    public List<String> getAuthors() {
        return new ArrayList<>(this.authors);
    }

    public void setAuthors(List<String> authors) {
        this.authors = new ArrayList<>(authors);
        updateTimestamp();
    }

    public String getAuthor() {
        return this.authors != null && !this.authors.isEmpty() ? this.authors.get(0) : null;
    }

    public void setAuthor(String author) {
        if (this.authors == null) {
            this.authors = new ArrayList<>();
        }
        this.authors.clear();
        this.authors.add(author);
        updateTimestamp();
    }

    public String getPublisher() {
        return this.publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
        updateTimestamp();
    }

    public LocalDate getPublishedDate() {
        return this.publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
        updateTimestamp();
    }

    public String getIsbn() {
        return this.isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
        updateTimestamp();
    }

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    public DocumentStatus getStatus() {
        return this.status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
        updateTimestamp();
    }

    public LocalDate getBorrowDate() {
        return this.borrowDate;
    }

    // Borrowing setters
    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public long getBorrowerId() {
        return this.borrowerId;
    }

    public void setBorrowerId(long borrowerId) {
        this.borrowerId = borrowerId;
    }

    public void addAuthor(String author) {
        if (this.authors == null) {
            this.authors = new ArrayList<>();
        }
        if (!this.authors.contains(author)) {
            this.authors.add(author);
            updateTimestamp();
        }
    }

    // Status check methods
    public boolean isAvailableForLoan() {
        return this.status == DocumentStatus.AVAILABLE;
    }

    public boolean isBorrowed() {
        return this.status == DocumentStatus.BORROWED;
    }

    // Common return method
    public void returnDocument() {
        if (isBorrowed()) {
            this.status = DocumentStatus.AVAILABLE;
            this.borrowDate = null;
            this.dueDate = null;
            this.borrowerId = 0;
            updateTimestamp();
        } else {
            throw new IllegalStateException("Document is not currently borrowed.");
        }
    }

    // Abstract methods
    public abstract String getDocumentType();

    public abstract void borrow(long borrowerId, int loanDays);

}