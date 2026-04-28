package fr.eni.bookhub_back.book;

import fr.eni.bookhub_back.common.ServiceResponse;

import fr.eni.bookhub_back.loan.Loan;
import fr.eni.bookhub_back.locale.LocaleHelper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200") // Restreindre à notre frontend
@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class BookRestController {

    private BookService bookService;
    private LocaleHelper localeHelper;

    private final static Logger logger = LoggerFactory.getLogger(BookRestController.class);

    @GetMapping("/books")
        public ResponseEntity<?> allBooks(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                          @RequestParam(name = "size", defaultValue = "20") Integer size,
                                          @RequestParam(name = "sort", defaultValue = "title") String sort,
                                          @RequestParam(name = "search", defaultValue = "") String search,
                                          @RequestParam(name = "categories", defaultValue = "ALL") List<String> categories, // defaultValue attend une string -> Spring va automatiquement convertir "ALL" en List<String> contenant ["ALL"].
                                          @RequestParam(name = "availability", defaultValue = "ALL") String availability) {
            return bookService.findBooks(page, size, sort, search, categories, availability);
        }

    @GetMapping("/books/{isbn}")
    public ResponseEntity<?> findBookByISBN(@PathVariable String isbn) {
        return bookService.findBookByISBN(isbn);
    }

    // Ajout de /id/{id} pour contourner l'erreur Ambiguous handler methods
    // car sinon, Spring ne sait pas quelle méthode appeler entre findBookByISBN() et findBookById()
    @GetMapping("/books/id/{id}")
    public ResponseEntity<?> findBookById(@PathVariable String id) {
        return bookService.findBookById(Integer.parseInt(id));
    }

    @PostMapping("/books/new")
    public ResponseEntity<ServiceResponse<Book>> createBook(@Valid @RequestBody Book book) {
        try {
            return bookService.createBook(book);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_CREATION_FAILED", localeHelper.i18n("book.creation-failed"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    @PutMapping("/books/{id}/update")
    public ResponseEntity<ServiceResponse<Book>> updateBook(@Valid @PathVariable String id, @RequestBody Book book) {
        try {
            return bookService.updateBook(Integer.parseInt(id.trim()), book);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_UPDATE_FAILED", localeHelper.i18n("book.update-failed-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    @DeleteMapping("/books/{id}/delete")
    public ResponseEntity<ServiceResponse<Book>> deleteBook(@Valid @PathVariable String id) {
        try {
            return bookService.deleteBook(Integer.parseInt(id.trim()));
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_DELETE_FAILED", localeHelper.i18n("book.delete-failed-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    @GetMapping("/dashboard/dashboardUserBookReadByUser")
    public ResponseEntity<ServiceResponse<List<Book>>> dashboardUserBookReadByUser(@RequestParam Integer userId){
        try {
            return bookService.dashboardUserBookReadByUser(userId);
        } catch (RuntimeException e){
            ServiceResponse<List<Book>> response = new ServiceResponse<>("LOAD_BOOK_FAILED", "{book.load-fail}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    @GetMapping("/dashboard/dashboardMostReadBooks")
    public ResponseEntity<ServiceResponse<List<Book>>> dashboardMostReadBooks(){
        try {
            return bookService.dashboardMostReadBooks();
        } catch (RuntimeException e){
            ServiceResponse<List<Book>> response = new ServiceResponse<>("LOAD_BOOK_FAILED", "{book.load-fail}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    @GetMapping("/dashboard/dashboardNbTotalBook")
    public ResponseEntity<ServiceResponse<Integer>> dashboardNbTotalBook(){
        try {
            return bookService.dashboardNbTotalBook();
        } catch (RuntimeException e){
            ServiceResponse<Integer> response = new ServiceResponse<>("LOAD_BOOK_FAILED", "{book.load-fail}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

//    @GetMapping("/api/books/loaned")
//    public ResponseEntity<ServiceResponse<List<Book>>> getAllLoanedBooksByUsername(String username){
//        return bookService.findLoanedBooksByUsername(username);
//    }
}
