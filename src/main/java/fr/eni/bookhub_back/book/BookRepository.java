package fr.eni.bookhub_back.book;

import fr.eni.bookhub_back.book.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends PagingAndSortingRepository<Book, Integer>, JpaRepository<Book, Integer> {
    Optional<Book> findBookByIsbn(String isbn);
    List<Book> findBooksByTitleContaining(String search);
    List<Book> findBooksByAuthorContains(String search);
    List<Book> findBooksByIsbnContaining(String search);
    List<Book> findBooksByCategory(Category category);
}
