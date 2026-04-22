package fr.eni.bookhub_back.book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/catalog")
public class BookRestController {

        @Autowired
        private BookRepository bookRepository;

        @GetMapping("/{page}")
        public ResponseEntity<?> allBooks(@PathVariable Integer page) {

            int size = 20;
            Pageable pageable = PageRequest.of(page, size);
            Page<Book> pageBook = bookRepository.findAll(pageable);

            return ResponseEntity.ok(pageBook.getContent());
        }
}
