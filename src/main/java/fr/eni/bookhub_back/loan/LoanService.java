package fr.eni.bookhub_back.loan;

import fr.eni.bookhub_back.book.Book;
import fr.eni.bookhub_back.book.BookRepository;
import fr.eni.bookhub_back.book.BookService;
import fr.eni.bookhub_back.book.bookcopy.BookCopy;
import fr.eni.bookhub_back.book.bookcopy.BookCopyRepository;
import fr.eni.bookhub_back.common.ServiceResponse;
import fr.eni.bookhub_back.user.bo.User;
import fr.eni.bookhub_back.user.dao.jpa.UserJpaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    private final static Logger logger = LoggerFactory.getLogger(LoanService.class);

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserJpaRepository userRepository;

    @Autowired
    BookCopyRepository bookCopyRepository;


    ResponseEntity<ServiceResponse<Loan>> reserverLivre(LoanDTO loanDTO){

        if (loanDTO.isbn == null || loanDTO.username == null) {
            ServiceResponse<Loan> response = new ServiceResponse<>("BOOK_NULL", "{book.null-error}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        Optional<User> optUser = userRepository.findByUsername(loanDTO.username);
        if (optUser.isEmpty()) {
            ServiceResponse<Loan> response = new ServiceResponse<>("USER_DOES_NOT_EXIST", "{user.not-found}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        Optional<Book> optBook = bookRepository.findBookByIsbn(loanDTO.isbn);

        if (optBook.isEmpty()) {
            ServiceResponse<Loan> response = new ServiceResponse<>("BOOK_DOES_NOT_EXIST", "{book.isbn-doesnt-exist-error}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        List<BookCopy> bookCopies = bookCopyRepository.findAllByBook(optBook.get());

        if(bookCopies.isEmpty()) {
            ServiceResponse<Loan> response = new ServiceResponse<>("BOOK_HAS_NO_COPIES", "{book.isbn-doesnt-exist-error}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);

        }

        BookCopy firstAvailableBook = checkAvailable(bookCopies);

        if(firstAvailableBook == null){
            ServiceResponse<Loan> response = new ServiceResponse<>("LOAN_SAVE_FAILED", "{loan.save-failed-error}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        try {
            Loan loan = Loan.builder()
                    .dateLoaned(LocalDate.now())
                    .bookcopy(firstAvailableBook)
                    .user(optUser.get())
                    .build();
            loanRepository.save(loan);
            ServiceResponse<Loan> response = new ServiceResponse<>("LOAN_SAVED", "{loan.saved-success}", loan);
            firstAvailableBook.setAvailable(false);
            bookCopyRepository.save(firstAvailableBook);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            ServiceResponse<Loan> response = new ServiceResponse<>("LOAN_SAVE_FAILED", "{loan.save-failed-error}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    BookCopy checkAvailable(List<BookCopy> bookCopies){
        for (BookCopy bookCopy : bookCopies) {
            if(!bookCopy.isAvailable()){
                return bookCopy;
            }
        }
        return null;
    }
}

