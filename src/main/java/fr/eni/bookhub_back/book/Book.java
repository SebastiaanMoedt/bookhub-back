package fr.eni.bookhub_back.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.eni.bookhub_back.book.bookcopy.BookCopy;
import fr.eni.bookhub_back.book.category.Category;
import fr.eni.bookhub_back.review.Review;
import fr.eni.bookhub_back.waitinglist.WaitingList;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(min = 10, max = 13)
    @NotBlank
    private String isbn;

    @Column(name = "TITLE", nullable = false)
    @Size(min = 4, max = 100)
    @NotBlank
    private String title;

    @Column(name = "AUTHOR", nullable = false)
    @Size(min = 2, max = 100)
    @NotBlank
    private String author;

    @JoinColumn(name = "CATEGORY")
    @ManyToOne
    private Category category;

    @Column(name = "COVER_URL")
    private String coverUrl;

    @Column(name = "DESCRIPTION", columnDefinition = "VARCHAR(MAX)")
    private String description;

}
