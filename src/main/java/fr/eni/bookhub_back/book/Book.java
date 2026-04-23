package fr.eni.bookhub_back.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.eni.bookhub_back.book.bookcopy.BookCopy;
import fr.eni.bookhub_back.book.category.Category;
import fr.eni.bookhub_back.review.Review;
import fr.eni.bookhub_back.waitinglist.WaitingList;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "BOOK")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ISBN", nullable = false, unique = true)
    private String isbn;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "AUTHOR", nullable = false)
    private String author;

    @JoinColumn(name = "CATEGORY")
    @ManyToOne
    private Category category;

    @Column(name = "COVER_URL")
    private String coverUrl;

    @Column(name = "DESCRIPTION", columnDefinition = "VARCHAR(MAX)")
    private String description;

}
