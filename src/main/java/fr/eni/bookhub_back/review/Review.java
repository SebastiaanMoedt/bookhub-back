package fr.eni.bookhub_back.review;

import fr.eni.bookhub_back.book.Book;
import fr.eni.bookhub_back.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @JoinColumn(nullable = false)
    private Book book;

    @Column(name = "SCORE")
    private int score;

    @Column(name = "COMMENT", nullable = false)
    private String comment;

    @Column(name = "DATE_PUBLISHED")
    private Date datePublished;
}
