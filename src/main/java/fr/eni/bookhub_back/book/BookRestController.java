package fr.eni.bookhub_back.book;
import fr.eni.bookhub_back.common.ServiceResponse;

import fr.eni.bookhub_back.common.ServiceResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/books")
public class BookRestController {

    @Autowired
    private BookService bookService;

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
        return bookService.createBook(book);
    }
}
