package net.justapie.majutsu.db.schema.book;

import java.time.LocalDate;
import java.util.List;

public class Book extends Document {
    private String type;
    private String language;
    private int pageCount;
    private final int DEFAULT_LOAN_DAYS = 14; 

  
    public Book(String title, List<String> authors, String publisher, String isbn,
                String type, String language, int pageCount) {
        super();
        this.title = title;
        this.authors = authors;  
        this.publisher = publisher;
        this.isbn = isbn;
        this.createdAt = LocalDate.now();
        this.status = DocumentStatus.AVAILABLE;
        this.type = type;
        this.language = language;
        this.pageCount = pageCount;
        updateTimestamp();
    }

    public String getType() {
        return type;
    }

    public String getLanguage() {
        return language;
    }

    public int getPageCount() {
        return pageCount;
    }

    @Override
    public String getDocumentType() {
        return "Book";
    }

    public void returnBook() {
        returnDocument();
    }
    @Override
    public int getDefaultLoanDays() {
        return DEFAULT_LOAN_DAYS; 
    }
}
