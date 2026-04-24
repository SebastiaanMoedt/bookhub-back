package fr.eni.bookhub_back.book;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.eni.bookhub_back.book.bookcopy.BookCopy;
import fr.eni.bookhub_back.book.category.Category;
import fr.eni.bookhub_back.review.Review;
import fr.eni.bookhub_back.waitinglist.WaitingList;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
//    @Min(value = 10, message = "{book.isbn.min-error}")
//    @Max(value = 13, message = "{book.isbn.max-error}")
    @NotBlank(message = "{book.isbn.blank-error}")
    private String isbn;

    @Column(name = "TITLE", nullable = false)
//    @Min(value = 4, message = "{book.title.min-error}")
//    @Max(value = 255, message = "{book.title.max-error}")
    @NotBlank(message = "{book.title.blank-error}")
    private String title;

    @Column(name = "AUTHOR", nullable = false)
//    @Min(value = 2, message = "{book.author.min-error}")
//    @Max(value = 255, message = "{book.author.max-error}")
    @NotBlank(message = "{book.author.blank-error}")
    private String author;

    @JoinColumn(name = "CATEGORY")
    @ManyToOne
    private Category category;

    @Column(name = "COVER_URL")
    private String coverUrl;

    @Column(name = "DESCRIPTION", columnDefinition = "VARCHAR(MAX)")
    private String description;

}
