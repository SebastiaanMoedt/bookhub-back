package fr.eni.bookhub_back.book.bookcopy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/books/copy")
public class BookCopyRestController {

    @Autowired
    BookCopyService bookCopyService;


    @GetMapping("/{isbn}")
    public ResponseEntity<?> findBookCopies(@PathVariable String isbn){
        return bookCopyService.findAllByBook(isbn);
    }
}
