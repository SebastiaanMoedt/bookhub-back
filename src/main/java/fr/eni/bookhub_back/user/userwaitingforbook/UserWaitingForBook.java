package fr.eni.bookhub_back.user.userwaitingforbook;

import fr.eni.bookhub_back.book.Book;
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

@Document(collection="userWaitingForBook")
public class UserWaitingForBook {
    private User user;
    private Date dateAdded;
    private Book book;
}
