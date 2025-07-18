package net.justapie.majutsu.db.schema.book;

import java.time.LocalDate;
import java.util.List;

public class Paper extends Document {
    private String doi;


    // Default constructor
    public Paper() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
        this.status = DocumentStatus.AVAILABLE;
    }


    // Constructor với basic fields
    public Paper(String title, List<String> author, String publisher, String isbn, String doi) {
        this.title = title;
        this.setAuthors(authors);
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


    public void borrow(long borrowerId) {
        borrow(borrowerId, 7); // Default 7 days for papers
    }


    public void returnPaper() {
        returnDocument();
    }
}