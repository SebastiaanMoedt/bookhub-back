package fr.eni.bookhub_back.book.bookcopy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.eni.bookhub_back.book.Book;
import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn(name = "BOOK_ID", referencedColumnName = "id")
    @ToString.Exclude        // ← évite book → copies → book → copies...
    @EqualsAndHashCode.Exclude
    @JsonIgnore // ← évite la récursivité JSON sans casser la sérialisation de Book
    private Book book;

    @Column(name = "IS_AVAILABLE")
    private boolean isAvailable;

    @Enumerated(EnumType.STRING)
    private BookState state;
}
