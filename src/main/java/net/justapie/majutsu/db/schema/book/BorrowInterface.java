package net.justapie.majutsu.db.schema.book;


interface BorrowInterface {
    // This interface combines all document-related functionality from specialized interfaces
    void borrow(long borrowerId, int loanDays);
    void returnDocument();
    boolean isAvailableForLoan();
    boolean isBorrowed();
    void borrow(long borrowerId);
    
}

