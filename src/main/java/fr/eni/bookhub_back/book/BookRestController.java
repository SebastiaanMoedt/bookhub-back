package fr.eni.bookhub_back.book;
import fr.eni.bookhub_back.common.ServiceResponse;
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
        public ResponseEntity<ServiceResponse<List<Book>>> allBooksPage0(){
            return bookService.findBooksByPage(0);
        }
        @GetMapping("/{page}")
        public ResponseEntity<?> allBooks(@PathVariable(required = false) Integer page) {
            return bookService.findBooksByPage(page);
        }

        @GetMapping("/detail/{isbn}")
        public ResponseEntity<?> findBookByISBN(@PathVariable String isbn){
            return bookService.findBookByISBN(isbn);
        }


}
