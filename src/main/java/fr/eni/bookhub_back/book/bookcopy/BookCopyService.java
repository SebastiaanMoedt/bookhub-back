package fr.eni.bookhub_back.book.bookcopy;

import fr.eni.bookhub_back.book.Book;
import fr.eni.bookhub_back.book.BookRepository;
import fr.eni.bookhub_back.book.BookService;
import fr.eni.bookhub_back.common.ServiceResponse;
import fr.eni.bookhub_back.locale.LocaleHelper;
import lombok.AllArgsConstructor;
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

import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class BookCopyService {

    private BookCopyRepository bookCopyRepository;
    private BookRepository bookRepository;
    private LocaleHelper localeHelper;
    private BookService bookService;

    private final static Logger logger = LoggerFactory.getLogger(BookCopyService.class);
    // TODO: INTEGRER AU SERVICE NOTIF

    public ResponseEntity<ServiceResponse<List<BookCopy>>> findAllByBook(String isbn) {
        try {
            Book book = bookRepository.findBookByIsbn(isbn).get();
            List<BookCopy> bookCopies = bookCopyRepository.findAllByBook(book);
            if (!bookCopies.isEmpty()) {
                ServiceResponse<List<BookCopy>> response =
                        new ServiceResponse<>("COPIES_FOUND", "Copies Trouvées", bookCopies);
                return ResponseEntity.ok(response);
            }
        } catch (RuntimeException e) {
            ServiceResponse<List<BookCopy>> response =
                    new ServiceResponse<>("BOOK_NOT_FOUND", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ServiceResponse<>("COPIES_NOT_FOUND", "Il n'y a pas de copies de ce livre.", null));
    }

    // TODO: Pouvoir créer plusieurs copies d'un coup
    public ResponseEntity<ServiceResponse<BookCopy>> createBookCopy(int bookId, BookCopy bookCopy) {
        Optional<Book> book = bookRepository.findById(bookId);

        if(book.isEmpty()) {
            ServiceResponse<BookCopy> response =
                    new ServiceResponse<>("BOOK_NOT_FOUND", localeHelper.i18n("book.not-found"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        try {
            bookCopy.setBook(book.get());
            bookCopyRepository.save(bookCopy);
            ServiceResponse<BookCopy> response =
                    new ServiceResponse<>("BOOK_COPY_CREATION_SUCCESS", localeHelper.i18n("book-copy.create-success"), bookCopy);
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        } catch (RuntimeException e) {
            ServiceResponse<BookCopy> response =
                    new ServiceResponse<>("BOOK_COPY_CREATION_FAILED", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    // TODO: Tester cette méthode
    public ResponseEntity<ServiceResponse<BookCopy>> deleteBookCopy(int id) {
        Optional<BookCopy> bookCopyInDB = bookCopyRepository.findById(id);
        if (bookCopyInDB.isEmpty()) {
            ServiceResponse<BookCopy> response = new ServiceResponse<>("BOOK_COPY_ID_DOESNT_EXIST", localeHelper.i18n("book-copy.id-doesnt-exist-error"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        try {
            bookCopyRepository.deleteById(id);
            ServiceResponse<BookCopy> response = new ServiceResponse<>("BOOK_COPY_DELETE_SUCCESS", localeHelper.i18n("book-copy.delete-success"));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            logger.error("BookCopyService deleteBookCopy() error : {}", e.getMessage());
            ServiceResponse<BookCopy> response = new ServiceResponse<>("BOOK_COPY_DELETE_FAILED", localeHelper.i18n("book-copy.delete-failed-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

    }

}
