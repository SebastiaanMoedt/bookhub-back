package fr.eni.bookhub_back.loan;

import fr.eni.bookhub_back.book.Book;
import fr.eni.bookhub_back.book.BookRepository;
import fr.eni.bookhub_back.book.bookcopy.BookCopy;
import fr.eni.bookhub_back.book.bookcopy.BookCopyRepository;
import fr.eni.bookhub_back.common.ServiceResponse;
import fr.eni.bookhub_back.locale.LocaleHelper;
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

    @Autowired
    private LocaleHelper localeHelper;

    ResponseEntity<ServiceResponse<List<Loan>>> dashboardUserLoanOngoing(Integer userId){
        try {
            List<Loan> loans = loanRepository.dashboardUserLoanOngoing(userId);
            ServiceResponse<List<Loan>> response = new ServiceResponse<>("LOAD_LOAN_SUCCESS", localeHelper.i18n("loan.load-success"), loans);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            ServiceResponse<List<Loan>> response = new ServiceResponse<>("LOAD_LOAN_FAIL", localeHelper.i18n("loan.load-failed-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    ResponseEntity<ServiceResponse<List<Loan>>> dashboardBiblioLoanOngoing(){
        try {
            List<Loan> loans = loanRepository.dashboardBiblioLoanOngoing();
            ServiceResponse<List<Loan>> response = new ServiceResponse<>("LOAD_LOAN_SUCCESS", localeHelper.i18n("loan.load-success"), loans);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            ServiceResponse<List<Loan>> response = new ServiceResponse<>("LOAD_LOAN_FAIL", localeHelper.i18n("loan.load-failed-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    ResponseEntity<ServiceResponse<List<Loan>>> dashboardUserLoanRetards(Integer userId){
        try {
            List<Loan> loans = loanRepository.dashboardUserLoanRetards(userId);
            ServiceResponse<List<Loan>> response = new ServiceResponse<>("LOAD_LOAN_SUCCESS", localeHelper.i18n("loan.load-success"), loans);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            ServiceResponse<List<Loan>> response = new ServiceResponse<>("LOAD_LOAN_FAIL", localeHelper.i18n("loan.load-failed-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    ResponseEntity<ServiceResponse<List<Loan>>> dashboardBiblioLoanRetards(){
        try {
            List<Loan> loans = loanRepository.dashboardBiblioLoanRetards();
            ServiceResponse<List<Loan>> response = new ServiceResponse<>("LOAD_LOAN_SUCCESS", localeHelper.i18n("loan.load-success"), loans);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            ServiceResponse<List<Loan>> response = new ServiceResponse<>("LOAD_LOAN_FAIL", localeHelper.i18n("loan.load-failed-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    ResponseEntity<ServiceResponse<Loan>> reserverLivre(LoanDTO loanDTO){

        if (loanDTO.isbn == null || loanDTO.username == null) {
            ServiceResponse<Loan> response = new ServiceResponse<>("BOOK_NULL", localeHelper.i18n("book.null-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        Optional<User> optUser = userRepository.findByUsername(loanDTO.username);
        if (optUser.isEmpty()) {
            ServiceResponse<Loan> response = new ServiceResponse<>("USER_DOES_NOT_EXIST", localeHelper.i18n("user.not-found"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        Optional<Book> optBook = bookRepository.findBookByIsbn(loanDTO.isbn);

        if (optBook.isEmpty()) {
            ServiceResponse<Loan> response = new ServiceResponse<>("BOOK_DOES_NOT_EXIST", localeHelper.i18n("book.isbn-doesnt-exist-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        List<BookCopy> bookCopies = bookCopyRepository.findAllByBook(optBook.get());

        if(bookCopies.isEmpty()) {
            ServiceResponse<Loan> response = new ServiceResponse<>("BOOK_HAS_NO_COPIES", localeHelper.i18n("book.isbn-doesnt-exist-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);

        }

        BookCopy firstAvailableBook = checkAvailable(bookCopies);

        if(firstAvailableBook == null){
            ServiceResponse<Loan> response = new ServiceResponse<>("LOAN_SAVE_FAILED", localeHelper.i18n("loan.save-failed-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        try {
            Loan loan = Loan.builder()
                    .dateLoaned(LocalDate.now())
                    .bookcopy(firstAvailableBook)
                    .user(optUser.get())
                    .build();
            loanRepository.save(loan);
            firstAvailableBook.setAvailable(false);
            bookCopyRepository.save(firstAvailableBook);
            ServiceResponse<Loan> response = new ServiceResponse<>("LOAN_SAVED", localeHelper.i18n("loan.saved-success"), loan);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            ServiceResponse<Loan> response = new ServiceResponse<>("LOAN_SAVE_FAILED", localeHelper.i18n("loan.save-failed-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    BookCopy checkAvailable(List<BookCopy> bookCopies){
        for (BookCopy bookCopy : bookCopies) {
            if(bookCopy.isAvailable()){
                return bookCopy;
            }
        }
        return null;
    }

    // TODO : updateLoan = Retour d'un livre
}

