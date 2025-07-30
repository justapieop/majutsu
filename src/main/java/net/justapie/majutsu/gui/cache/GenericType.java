package net.justapie.majutsu.gui.cache;

import java.time.LocalDate;

public class GenericType {
    private long id;
    private String title;
    private String publisher;
    private LocalDate publishDate;
    private String isbn;
    private String language;
    private int pageCount;


    private String borrowedBy;
    private LocalDate borrowedAt;
    private LocalDate dueDate;
    private boolean Borrowed;

    private LocalDate createdAt;
    private LocalDate updatedAt;


}
