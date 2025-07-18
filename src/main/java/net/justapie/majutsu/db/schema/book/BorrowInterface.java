package net.justapie.majutsu.db.schema.book;


interface BorrowInterface {
    void borrow(long borrowerId, int loanDays);
    void returnDocument();
    boolean isAvailableForLoan();
    boolean isBorrowed();
    void borrow(long borrowerId);
    
}

