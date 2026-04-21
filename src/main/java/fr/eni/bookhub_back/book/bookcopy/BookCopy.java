package fr.eni.bookhub_back.book.bookcopy;

import fr.eni.bookhub_back.book.Book;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "BOOK_COPY")
public class BookCopy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ISBN")
    private Book book;

    @Column(name = "IS_AVAILABLE")
    private boolean isAvailable;

    @Enumerated(EnumType.STRING)
    private BookState state;
}
