package fr.eni.bookhub_back.book;

import fr.eni.bookhub_back.common.ServiceResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BookService {

    // TODO: inject and use MessageResolver

    private BookRepository bookRepository;

    private final static Logger logger = LoggerFactory.getLogger(BookService.class);

    public ResponseEntity<ServiceResponse<List<Book>>> findBooksByPage(int page) {

        int size = 20;
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Book> pageBook = bookRepository.findAll(pageable);
            ServiceResponse<List<Book>> response =
                    new ServiceResponse<>("BOOKS_FOUND", "Livres trouvés", pageBook.getContent());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ServiceResponse<List<Book>> response =
                    new ServiceResponse<>("BOOKS_NOT_FOUND", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public ResponseEntity<ServiceResponse<Book>> findBookByISBN(String isbn) {
        try {
            Book book = bookRepository.findBookByIsbn(isbn).get();
            ServiceResponse<Book> response =
                    new ServiceResponse<>("BOOK_FOUND", "Livre trouvé", book);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ServiceResponse<Book> response =
                    new ServiceResponse<>("BOOK_NOT_FOUND", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public ResponseEntity<ServiceResponse<Book>> createBook(Book b) {
        if (b == null) {
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_NULL", "{book.null-error}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        if (b.getTitle() == null || b.getTitle().isBlank()) {
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_TITLE_BLANK", "{book.title.blank-error}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        if (b.getAuthor() == null || b.getAuthor().isBlank()) {
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_AUTHOR_BLANK", "{book.author.blank-error}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        Optional<Book> optBook = bookRepository.findBookByIsbn(b.getIsbn());

        if (optBook.isPresent()) {
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_ALREADY_EXISTS", "{book.isbn-already-exists-error}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        try {
            bookRepository.save(b);
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_CREATE_SUCCESS", "{book.create-success}", b);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_SAVE_FAILED", "{book.save-failed-error}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }
}
