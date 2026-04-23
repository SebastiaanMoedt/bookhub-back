package fr.eni.bookhub_back.book.bookcopy;

import fr.eni.bookhub_back.book.Book;
import fr.eni.bookhub_back.book.BookRepository;
import fr.eni.bookhub_back.common.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookCopyService {

    @Autowired
    private BookCopyRepository bookCopyRepository;
    @Autowired
    private BookRepository bookRepository;

    // TODO: INTEGRER AU SERVICE NOTIF

    public ResponseEntity<ServiceResponse<List<BookCopy>>> findAllByBook(String isbn) {
        try {
            Book book = bookRepository.findBookByIsbn(isbn).get();
            List<BookCopy> bookCopies = bookCopyRepository.findAllByBook(book);
            if(!bookCopies.isEmpty()){
                ServiceResponse<List<BookCopy>> response =
                        new ServiceResponse<>("COPIES_FOUND", "Copies Trouvées", bookCopies);
                return ResponseEntity.ok(response);
            }
        } catch (RuntimeException e) {
            ServiceResponse<List<BookCopy>> response =
                    new ServiceResponse<>("BOOK_NOT_FOUND", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ServiceResponse<>("COPIES_NOT_FOUND", "Il n'y a pas de copies de ce livre.", null));
    }
}
