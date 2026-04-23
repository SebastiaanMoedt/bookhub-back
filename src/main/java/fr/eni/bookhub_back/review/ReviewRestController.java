package fr.eni.bookhub_back.review;
import fr.eni.bookhub_back.book.BookRepository;
import fr.eni.bookhub_back.book.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/books/reviews")
public class ReviewRestController {

    @Autowired
    ReviewService reviewService;

    @GetMapping("")
    public ResponseEntity<?> reviewsByBook(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                      @RequestParam(name = "size", defaultValue = "10") Integer size,
                                      @RequestParam(name = "sort", defaultValue = "datePublished") String sort,
                                           @RequestParam(name = "isbn") String isbn) {

        return reviewService.findAllByBook(page, size, sort, isbn);
    }
}
