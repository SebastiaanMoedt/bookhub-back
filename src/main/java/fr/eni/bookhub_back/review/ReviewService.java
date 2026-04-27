package fr.eni.bookhub_back.review;

import fr.eni.bookhub_back.book.Book;
import fr.eni.bookhub_back.book.BookRepository;
import fr.eni.bookhub_back.book.BookService;
import fr.eni.bookhub_back.book.bookcopy.BookCopy;
import fr.eni.bookhub_back.common.ServiceResponse;
import fr.eni.bookhub_back.loan.Loan;
import fr.eni.bookhub_back.loan.LoanService;
import fr.eni.bookhub_back.locale.LocaleHelper;
import fr.eni.bookhub_back.user.bo.User;
import fr.eni.bookhub_back.user.dao.jpa.UserJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    BookService bookService;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    private LocaleHelper localeHelper;
    @Autowired
    private UserJpaRepository userRepository;

    private final static Logger logger = LoggerFactory.getLogger(ReviewService.class);

    public ResponseEntity<ServiceResponse<Page<Review>>> findAllByBook(int page, int size, String sortBy, String isbn) {
        try {
            Book book = bookService.findBookObjectByISBN(isbn).get();
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).descending());
            Page<Review> pageReview = reviewRepository.findAllByBook(pageable, book);
            ServiceResponse<Page<Review>> response =
                    new ServiceResponse<>("REVIEWS_FOUND", "Commentaires trouvés", pageReview);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ServiceResponse<Page<Review>> response =
                    new ServiceResponse<>("REVIEWS_NOT_FOUND", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public ResponseEntity<ServiceResponse<Review>> saveReview(ReviewDTO reviewDTO){

        if (reviewDTO.bookISBN == null || reviewDTO.username == null) {
            ServiceResponse<Review> response = new ServiceResponse<>("BOOK_NULL", localeHelper.i18n("book.null-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        Optional<User> optUser = userRepository.findByUsername(reviewDTO.username);
        if (optUser.isEmpty()) {
            ServiceResponse<Review> response = new ServiceResponse<>("USER_DOES_NOT_EXIST", localeHelper.i18n("user.not-found"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        Optional<Book> optBook = bookRepository.findBookByIsbn(reviewDTO.bookISBN);

        if (optBook.isEmpty()) {
            ServiceResponse<Review> response = new ServiceResponse<>("BOOK_DOES_NOT_EXIST", localeHelper.i18n("book.isbn-doesnt-exist-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        if(reviewDTO.comment == null){
            ServiceResponse<Review> response = new ServiceResponse<>("REVIEW_IS_EMPTY", localeHelper.i18n("review.comment-empty-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        try {
            Review review = Review.builder()
                    .datePublished(LocalDate.now())
                    .book(optBook.get())
                    .user(optUser.get())
                    .score(reviewDTO.score).comment(reviewDTO.comment).build();
            reviewRepository.save(review);
            ServiceResponse<Review> response = new ServiceResponse<>("REVIEW_SAVED", localeHelper.i18n("review.saved-success"), review);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            ServiceResponse<Review> response = new ServiceResponse<>("REVIEW_SAVE_FAILED", localeHelper.i18n("review.save-failed-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }
}
