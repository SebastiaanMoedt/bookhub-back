package fr.eni.bookhub_back.review;

import fr.eni.bookhub_back.book.Book;
import fr.eni.bookhub_back.book.BookRepository;
import fr.eni.bookhub_back.book.BookService;
import fr.eni.bookhub_back.common.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    BookService bookService;

    public ResponseEntity<ServiceResponse<Page<Review>>> findAllByBook(int page, int size, String sortBy, String isbn) {
        try {
            Book book = bookService.findBookObjectByISBN(isbn).get();
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
            Page<Review> pageReview = reviewRepository.findAllByBook(pageable, book);
            if(pageReview.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ServiceResponse<>("REVIEWS_NOT_FOUND",null));
            }
            ServiceResponse<Page<Review>> response =
                    new ServiceResponse<>("REVIEWS_FOUND", "Commentaires trouvés", pageReview);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ServiceResponse<Page<Review>> response =
                    new ServiceResponse<>("REVIEWS_NOT_FOUND", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
