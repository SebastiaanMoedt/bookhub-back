package fr.eni.bookhub_back.book;

import fr.eni.bookhub_back.common.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public ResponseEntity<ServiceResponse<List<Book>>> findBooksByPage(int page) {

        int size = 20;
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Book> pageBook = bookRepository.findAll(pageable);
            System.out.println(pageBook);
            ServiceResponse<List<Book>> response =
                    new ServiceResponse<>("BOOKS_FOUND", "Livres trouvés", pageBook.getContent());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ServiceResponse<List<Book>> response =
                    new ServiceResponse<>("BOOKS_NOT_FOUND", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public ResponseEntity<ServiceResponse<Book>> findBookByISBN(String isbn){
        try {
            Book book = bookRepository.findBookByIsbn(isbn).get();
            ServiceResponse<Book> response =
                    new ServiceResponse<>("BOOK_FOUND", "Livre trouvé", book);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ServiceResponse<Book> response =
                    new ServiceResponse<>("BOOK_NOT_FOUND", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
    }
}
