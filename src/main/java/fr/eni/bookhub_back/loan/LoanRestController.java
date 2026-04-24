package fr.eni.bookhub_back.loan;
import fr.eni.bookhub_back.book.Book;
import fr.eni.bookhub_back.book.bookcopy.BookCopy;
import fr.eni.bookhub_back.common.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class LoanRestController {

    @Autowired
    LoanService loanService;

    @PostMapping("/api/books/loan")
    public ResponseEntity<ServiceResponse<Loan>> reserverLivre(@RequestBody LoanDTO loanDTO){
        try {
            return loanService.reserverLivre(loanDTO);
        } catch (RuntimeException e){
            ServiceResponse<Loan> response = new ServiceResponse<>("LOAN_SAVE_FAILED", "{loan.save-failed-error}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }
}
