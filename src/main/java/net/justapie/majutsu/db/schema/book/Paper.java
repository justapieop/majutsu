package net.justapie.majutsu.db.schema.book;

import java.time.LocalDate;

public class Paper extends Document {
    private String doi;


    // Default constructor
    public Paper() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
        this.status = DocumentStatus.AVAILABLE;
    }


    // Constructor với basic fields
    public Paper(String title, String author, String publisher, String isbn, String doi) {
        this.title = title;
        this.setAuthor(author);
        this.publisher = publisher;
        this.isbn = isbn;
        this.doi = doi;
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
        this.status = DocumentStatus.AVAILABLE;
    }


    // Getters
    public String getDoi() {
        return this.doi;
    }


    // Setters
    public void setDoi(String doi) {
        this.doi = doi;
    }


    @Override
    public String getDocumentType() {
        return "Paper";
    }


    @Override
    public void borrow(long borrowerId, int loanDays) {
        if (isAvailableForLoan()) {
            this.status = DocumentStatus.BORROWED;
            this.borrowerId = borrowerId;
            this.borrowDate = LocalDate.now();
            this.dueDate = this.borrowDate.plusDays(loanDays);
            this.updatedAt = LocalDate.now();
        } else {
            throw new IllegalStateException("Paper is not available for loan.");
        }
    }


    public void borrow(long borrowerId) {
        borrow(borrowerId, 7); // Default 7 days for papers
    }


    public void returnPaper() {
        returnDocument();
    }
}