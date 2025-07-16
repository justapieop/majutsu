package net.justapie.majutsu.db.schema.book;

import java.time.LocalDate;
import java.util.List;

public class Book extends Document {
    private String type;
    private String language;
    private int pageCount;

    public Book() {
        super();
    }

    // Constructor với một tác giả
    public Book(String title, String author, String publisher, String isbn,
                String type, String language, int pageCount) {
        super();
        this.title = title;
        this.setAuthor(author);  // Sử dụng setter để xử lý List<String> authors
        this.publisher = publisher;
        this.isbn = isbn;
        this.createdAt = LocalDate.now();
        this.status = DocumentStatus.AVAILABLE;
        this.type = type;
        this.language = language;
        this.pageCount = pageCount;
        updateTimestamp();
    }

    // Constructor với nhiều tác giả
    public Book(String title, List<String> authors, String publisher, String isbn,
                String type, String language, int pageCount) {
        super();
        this.title = title;
        this.setAuthors(authors);  // Sử dụng setter để xử lý List<String> authors
        this.publisher = publisher;
        this.isbn = isbn;
        this.createdAt = LocalDate.now();
        this.status = DocumentStatus.AVAILABLE;
        this.type = type;
        this.language = language;
        this.pageCount = pageCount;
        updateTimestamp();
    }

    // Getters
    public String getType() {
        return type;
    }

    // Setters
    public void setType(String type) {
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public String getDocumentType() {
        return "Book";
    }

    @Override
    public void borrow(long borrowerId, int loanDays) {
        if (isAvailableForLoan()) {
            this.status = DocumentStatus.BORROWED;
            this.borrowerId = borrowerId;
            this.borrowDate = LocalDate.now();
            this.dueDate = this.borrowDate.plusDays(loanDays);
            updateTimestamp();
        } else {
            throw new IllegalStateException("Book is not available for loan.");
        }
    }


    public void borrow(long borrowerId) {
        borrow(borrowerId, 14);
    }


    public void returnBook() {
        returnDocument();
    }
}