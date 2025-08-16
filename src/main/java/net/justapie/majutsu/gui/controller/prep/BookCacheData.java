package net.justapie.majutsu.gui.controller.prep;

import net.justapie.majutsu.db.schema.book.Book;

public class BookCacheData {
    private Book book;
    private BookStatusType status;

    public BookCacheData(Book book, BookStatusType status) {
        this.book = book;
        this.status = status;
    }

    public Book getBook() {
        return this.book;
    }

    public BookStatusType getStatus() {
        return this.status;
    }
}
