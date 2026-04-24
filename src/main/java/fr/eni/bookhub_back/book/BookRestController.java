package fr.eni.bookhub_back.book;
import fr.eni.bookhub_back.common.ServiceResponse;

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
    private final static Logger logger = LoggerFactory.getLogger(BookRestController.class);


    @GetMapping
        public ResponseEntity<?> allBooks(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                          @RequestParam(name = "size", defaultValue = "10") Integer size,
                                          @RequestParam(name = "sort", defaultValue = "title") String sort) {
            return bookService.findBooks(page, size, sort);
        }

        @GetMapping("/{isbn}")
        public ResponseEntity<?> findBookByISBN(@PathVariable String isbn){

            return bookService.findBookByISBN(isbn);
        }

    @PostMapping("/new")
    public ResponseEntity<ServiceResponse<Book>> saveBook(@Valid @RequestBody Book book) {
        try {
            return bookService.createBook(book);
        } catch (RuntimeException e){
            logger.error(e.getMessage());
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_SAVE_FAILED", "{book.save-failed-error}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

    }
}
