package fr.eni.bookhub_back.review;

import fr.eni.bookhub_back.book.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface ReviewRepository extends PagingAndSortingRepository<Review, Integer>, JpaRepository<Review, Integer> {
    Page<Review> findAllByBook(Pageable pageable, Book book);
}
