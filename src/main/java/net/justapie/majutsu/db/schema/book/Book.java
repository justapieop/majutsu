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
    

    //Multiple authors
    public Book(String title, List<String> authors, String publisher, String isbn,
                String type, String language, int pageCount) {
        super();
        this.title = title;
        this.setAuthors(authors);  
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

    public void borrow(long borrowerId) {
        borrow(borrowerId, 14);
    }

    public void returnBook() {
        returnDocument();
    }
}