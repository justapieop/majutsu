package net.justapie.majutsu.db.schema.book;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import net.justapie.majutsu.db.schema.user.User;
import net.justapie.majutsu.db.schema.user.UserRole;
import net.justapie.majutsu.gbook.model.Volume;


import java.sql.ResultSet;


public class Book extends Volume{    
    private long borrowedBy;
    private LocalDate borrowedAt;
    private LocalDate expectedReturn;

    Book(){
        super();
    }

    public long getBorrowedBy(){
       return borrowedBy;
    }

    public LocalDate getBorrowedAt(){
        return borrowedAt;
    }

    public LocalDate expectedReturn(){
        return expectedReturn;
    }

    public static Book fromVolume(Volume volume){
        Book book = new Book();
        book.borrowedAt = null;
        book.expectedReturn = null;
        book.borrowedBy = 0;
        return book;
    }

    public static Book fromResultSet(ResultSet resultSet) {
        final Book book = new Book();

        try {
            book.borrowedAt = resultSet.getDate("borrowed_at").toLocalDate();
            book.borrowedBy = resultSet.getLong("borrowed_by");
            book.expectedReturn = resultSet.getDate("return_date").toLocalDate();

        } catch (SQLException e) {
            return null;
        }

        return book;
    }

    
   
}
