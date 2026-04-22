package fr.eni.bookhub_back.book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/api/books")
public class BookRestController {

        @Autowired
        private BookService bookService;

        @GetMapping
        public ResponseEntity<?> allBooks(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                          @RequestParam(name = "size", defaultValue = "20") Integer size,
                                          @RequestParam(name = "sort", defaultValue = "title") String sort) {
            return bookService.findBooks(page, size, sort);


        }

        @GetMapping("/detail/{isbn}")
        public ResponseEntity<?> findBookByISBN(@PathVariable String isbn){
            return bookService.findBookByISBN(isbn);
        }


}
