package net.justapie.majutsu.db.schema.book;

import java.util.List;

public abstract class DocumentFactory {
    public static Document createBook(String title, List<String> authors, String publisher, String isbn, String type, String language, int pageCount) {
        return new Book(title, authors, publisher, isbn, type, language, pageCount);
    }
    public static Document createPaper(String title, List<String> authors, String publisher, String isbn, String doi) {
        return new Paper(title, authors, publisher, isbn, doi);
    }
}
