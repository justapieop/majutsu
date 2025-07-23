package net.justapie.majutsu.db.schema.book;

import java.time.LocalDate;
import java.util.List;

public class Paper extends Document {
    private String doi;
    private final int DEFAULT_LOAN_DAYS = 7; 

    
    public Paper(String title, List<String> authors, String publisher, String isbn, String doi) {
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.isbn = isbn;
        this.doi = doi;
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
        this.status = DocumentStatus.AVAILABLE;
    }


    public String getDoi() {
        return this.doi;
    }

    @Override
    public String getDocumentType() {
        return "Paper";
    }


    public void returnPaper() {
        returnDocument();
    }

    @Override
    public int getDefaultLoanDays() {
        return DEFAULT_LOAN_DAYS; 
    }
}