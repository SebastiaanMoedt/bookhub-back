package fr.eni.bookhub_back.book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends PagingAndSortingRepository<Book, Integer>, JpaRepository<Book, Integer> {
    Optional<Book> findBookByIsbn(String isbn);
}
