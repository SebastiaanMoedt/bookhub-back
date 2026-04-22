package fr.eni.bookhub_back.book;
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
        private BookRepository bookRepository;

        @GetMapping
        public ResponseEntity<?> allBooksPage0(){
            int size = 20;
            Pageable pageable = PageRequest.of(0, size);
            Page<Book> pageBook = bookRepository.findAll(pageable);

            return ResponseEntity.ok(pageBook.getContent());
        }

        @GetMapping("/{page}")
        public ResponseEntity<?> allBooks(@PathVariable(required = false) Integer page) {
            int size = 20;
            Pageable pageable = PageRequest.of(page, size);
            Page<Book> pageBook = bookRepository.findAll(pageable);

            return ResponseEntity.ok(pageBook.getContent());
        }

        @GetMapping("/detail/{isbn}")
        public ResponseEntity<?> findBookByISBN(@PathVariable String isbn){
            Optional<Book> book = bookRepository.findBookByIsbn(isbn);
            if (book.isEmpty()){
                throw new RuntimeException("this book doesn't exist");
            }
            return ResponseEntity.ok(book.get());
        }

}
