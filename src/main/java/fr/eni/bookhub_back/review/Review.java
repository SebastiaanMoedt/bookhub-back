package fr.eni.bookhub_back.review;

import fr.eni.bookhub_back.book.Book;
import fr.eni.bookhub_back.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Document(collection="review")
public class Review {
    private User user;
    private Book book;
    private int score;
    private String comment;
}
