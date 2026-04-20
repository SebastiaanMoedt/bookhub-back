package fr.eni.bookhub_back.book;

import fr.eni.bookhub_back.book.bookcopy.BookCopy;
import fr.eni.bookhub_back.review.Review;
import fr.eni.bookhub_back.user.userwaitingforbook.UserWaitingForBook;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Document(collection="book")
public class Book {
    private String isbn;
    private String title;
    private String author;
    private String category;
    private String coverUrl;
    private List<Review> reviews;
    private List<UserWaitingForBook> waitingList;
    private List<BookCopy> copies;
}
