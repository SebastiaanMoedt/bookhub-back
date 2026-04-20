package fr.eni.bookhub_back.book.bookcopy;

import fr.eni.bookhub_back.book.Book;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Document(collection= "bookCopy")
public class BookCopy {
    private Integer id;
    private Book book;
    private boolean isAvailable;
    private BookState state;
}
