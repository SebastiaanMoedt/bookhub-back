package fr.eni.bookhub_back.book;

import fr.eni.bookhub_back.book.bookcopy.BookCopy;
import fr.eni.bookhub_back.book.category.Category;
import fr.eni.bookhub_back.review.Review;
import fr.eni.bookhub_back.waitinglist.WaitingList;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(name = "ISBN", nullable = false)
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

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    private @Builder.Default List<BookCopy> bookCopies = new ArrayList<>();

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    private @Builder.Default List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY)
    private @Builder.Default List<WaitingList> waitingList = new ArrayList<>();
}
