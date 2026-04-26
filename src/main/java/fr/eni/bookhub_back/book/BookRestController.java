package fr.eni.bookhub_back.book;

import fr.eni.bookhub_back.common.ServiceResponse;

import fr.eni.bookhub_back.locale.LocaleHelper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/api/books")
@AllArgsConstructor
public class BookRestController {

    private BookService bookService;
    private LocaleHelper localeHelper;

    private final static Logger logger = LoggerFactory.getLogger(BookRestController.class);

    @GetMapping
    public ResponseEntity<?> allBooks(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                      @RequestParam(name = "size", defaultValue = "20") Integer size,
                                      @RequestParam(name = "sort", defaultValue = "title") String sort) {
        return bookService.findBooks(page, size, sort);
    }

    @GetMapping("/{isbn}")
    public ResponseEntity<?> findBookByISBN(@PathVariable String isbn) {
        return bookService.findBookByISBN(isbn);
    }

    // Ajout de /id/{id} pour contourner l'erreur Ambiguous handler methods
    // car sinon, Spring ne sait pas quelle méthode appeler entre findBookByISBN() et findBookById()
    @GetMapping("/id/{id}")
    public ResponseEntity<?> findBookById(@PathVariable String id) {
        return bookService.findBookById(Integer.parseInt(id));
    }

    @PostMapping("/new")
    public ResponseEntity<ServiceResponse<Book>> createBook(@Valid @RequestBody Book book) {
        try {
            return bookService.createBook(book);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_CREATION_FAILED", localeHelper.i18n("book.creation-failed"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<ServiceResponse<Book>> updateBook(@Valid @PathVariable String id, @RequestBody Book book) {
        try {
            return bookService.updateBook(Integer.parseInt(id.trim()), book);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_UPDATE_FAILED", localeHelper.i18n("book.update-failed-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }
}
