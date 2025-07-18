package net.justapie.majutsu.db.schema.book;

import java.time.LocalDate;
import java.util.List;

interface DisplayInterface {
    long getId();
    String getTitle();
    List<String> getAuthors();
    String getAuthor();
    String getPublisher();
    LocalDate getPublishedDate();
    String getIsbn();
    LocalDate getCreatedAt();
    LocalDate getUpdatedAt();
    DocumentStatus getStatus();
    LocalDate getBorrowDate();
    LocalDate getDueDate();
    long getBorrowerId();
    String getDocumentType();
    int getDefaultLoanDays(); // Default loan period in days


}


interface BookDisplayInterface extends DisplayInterface {
    // Book-specific getters
    String getType();
    String getLanguage();
    int getPageCount();
}

interface PaperDisplayInterface extends DisplayInterface {
    // Paper-specific getter
    String getDoi();
}
