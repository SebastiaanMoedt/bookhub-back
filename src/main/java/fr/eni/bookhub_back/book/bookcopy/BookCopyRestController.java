package fr.eni.bookhub_back.book.bookcopy;

import fr.eni.bookhub_back.book.Book;
import fr.eni.bookhub_back.book.BookRestController;
import fr.eni.bookhub_back.common.ServiceResponse;
import fr.eni.bookhub_back.locale.LocaleHelper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/books/copy")
@AllArgsConstructor
public class BookCopyRestController {

    private LocaleHelper localeHelper;
    private BookCopyService bookCopyService;
    private final static Logger logger = LoggerFactory.getLogger(BookCopyRestController.class);

    @GetMapping("/{isbn}")
    public ResponseEntity<?> findBookCopies(@PathVariable String isbn){
        return bookCopyService.findAllByBook(isbn);
    }

    @PostMapping("/{bookId}/new-copy")
    public ResponseEntity<ServiceResponse<BookCopy>> createBookCopy(@Valid @RequestBody BookCopy bookCopy, @PathVariable String bookId) {
        try {
            return bookCopyService.createBookCopy(Integer.parseInt(bookId), bookCopy);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            ServiceResponse<BookCopy> response = new ServiceResponse<>("BOOK_COPY_CREATION_FAILED", localeHelper.i18n("book-copy.creation-failed"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ServiceResponse<BookCopy>> deleteBookCopy(@Valid @PathVariable String id) {
        try {
            return bookCopyService.deleteBookCopy(Integer.parseInt(id));
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            ServiceResponse<BookCopy> response = new ServiceResponse<>("BOOK_COPY_DELETE_FAILED", localeHelper.i18n("book-copy.delete-failed"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }
}
