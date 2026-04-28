package fr.eni.bookhub_back.book;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.eni.bookhub_back.book.bookcopy.BookCopy;
import fr.eni.bookhub_back.book.category.Category;
import fr.eni.bookhub_back.review.Review;
import fr.eni.bookhub_back.waitinglist.WaitingList;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "BOOK")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // ← dit à Jackson d'ignorer les propriétés internes du proxy Hibernate
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

    // LAZY = on ne récupère que la promotion
    // @Builder.Default = pour indiquer au builder d'instancier cette liste à vide et non à null

    // @TODO pour info... obligé de faire le lien pour que l'ORM gère le join de Book et Copie...
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private @Builder.Default Set<BookCopy> copies = new HashSet<>();

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private @Builder.Default Set<Review> reviews = new HashSet<>(); // ← nécessaire pour le join

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private @Builder.Default Set<WaitingList> reservations = new HashSet<>(); // ← nécessaire pour le join

}
