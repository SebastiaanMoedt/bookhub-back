package fr.eni.bookhub_back.borrow;

import fr.eni.bookhub_back.book.bookcopy.BookCopy;
import fr.eni.bookhub_back.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Document(collection= "borrow")
public class Borrow {
    private BookCopy bookcopy;
    private Date dateBorrowed;
    private Date dateReturned;
    private User user;
}
