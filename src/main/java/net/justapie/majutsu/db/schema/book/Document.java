package net.justapie.majutsu.db.schema.book;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public abstract class Document implements DisplayInterface, BorrowInterface{
    // Basic document properties
    protected long id;
    protected String title;
    protected List<String> authors;  
    protected String publisher;
    protected LocalDate publishedDate;
    protected String isbn;

    // Timestamps and status
    protected LocalDate createdAt;
    protected LocalDate updatedAt;
    protected DocumentStatus status;
    protected LocalDate borrowDate;
    protected LocalDate dueDate;
    protected long borrowerId;
    



     protected void updateTimestamp() {
        this.updatedAt = LocalDate.now();
    }


    public long getId() {
        return this.id;
    }


    public String getTitle() {
        return this.title;
    }


    public List<String> getAuthors() {
        return new ArrayList<>(this.authors);
    }

    public String getAuthor() {
        return this.authors.get(0); 
    }

    public String getPublisher() {
        return this.publisher;
    }


    public LocalDate getPublishedDate() {
        return this.publishedDate;
    }


    public String getIsbn() {
        return this.isbn;
    }

  

    public LocalDate getCreatedAt() {
        return this.createdAt;
    }

    public LocalDate getUpdatedAt() {
        return this.updatedAt;
    }

   


    public DocumentStatus getStatus() {
        return this.status;
    }

    public LocalDate getBorrowDate() {
        return this.borrowDate;
    }

    public LocalDate getDueDate() {
        return this.dueDate;
    }

    public long getBorrowerId() {
        return this.borrowerId;
    }

    public boolean isAvailableForLoan() {
        return this.status == DocumentStatus.AVAILABLE;
    }

    public boolean isBorrowed() {
        return this.status == DocumentStatus.BORROWED;
    }

    public void borrow(long borrowerId, int loanDays) {
       if (isAvailableForLoan()) {
           this.status = DocumentStatus.BORROWED;
           this.borrowerId = borrowerId;
           this.borrowDate = LocalDate.now();
           this.dueDate = this.borrowDate.plusDays(loanDays);
           updateTimestamp();
       } else {
           throw new IllegalStateException("Book is not available for loan.");
       }
   }
   public void borrow(long borrowerId) {
        borrow(borrowerId, getDefaultLoanDays()); 
    }
    
    public void returnDocument() {
        if (isBorrowed()) {
            this.status = DocumentStatus.AVAILABLE;
            this.borrowDate = null;
            this.dueDate = null;
            this.borrowerId = 0;
            updateTimestamp();
        } else {
            throw new IllegalStateException("Document is not currently borrowed.");
        }
    }
}