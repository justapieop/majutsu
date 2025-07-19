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
    int getDefaultLoanDays(); 


}


interface BookDisplayInterface extends DisplayInterface {
    String getType();
    String getLanguage();
    int getPageCount();
}

interface PaperDisplayInterface extends DisplayInterface {
    String getDoi();
}
