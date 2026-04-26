package fr.eni.bookhub_back.review;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.eni.bookhub_back.book.Book;
import fr.eni.bookhub_back.user.bo.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "REVIEW")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID", nullable = false, referencedColumnName = "id")
    @ToString.Exclude        // ← évite book → copies → book → copies...
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Book book;

    @Column(name = "SCORE")
    private int score;

    @Column(name = "COMMENT", nullable = false)
    private String comment;

    @Column(name = "DATE_PUBLISHED")
    private Date datePublished;
}
