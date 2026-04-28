package fr.eni.bookhub_back.loan;
import fr.eni.bookhub_back.book.Book;
import fr.eni.bookhub_back.book.bookcopy.BookCopy;
import fr.eni.bookhub_back.common.ServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("api/dashboard/dashboardUserLoanOngoing")
    public ResponseEntity<ServiceResponse<List<Loan>>> dashboardUserLoanOngoing(@RequestParam Integer userId){
        try {
            return loanService.dashboardUserLoanOngoing(userId);
        } catch (RuntimeException e){
            ServiceResponse<List<Loan>> response = new ServiceResponse<>("LOAD_LOAN_FAILED", "{loan.load-fail}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    @GetMapping("api/dashboard/dashboardBiblioLoanOngoing")
    public ResponseEntity<ServiceResponse<List<Loan>>> dashboardBiblioLoanOngoing(){
        try {
            return loanService.dashboardBiblioLoanOngoing();
        } catch (RuntimeException e){
            ServiceResponse<List<Loan>> response = new ServiceResponse<>("LOAD_LOAN_FAILED", "{loan.load-fail}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    @GetMapping("api/dashboard/dashboardUserLoanRetards")
    public ResponseEntity<ServiceResponse<List<Loan>>> dashboardUserLoanRetards(@RequestParam Integer userId){
        try {
            return loanService.dashboardUserLoanRetards(userId);
        } catch (RuntimeException e){
            ServiceResponse<List<Loan>> response = new ServiceResponse<>("LOAD_LOAN_FAILED", "{loan.load-fail}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    @GetMapping("api/dashboard/dashboardBiblioLoanRetards")
    public ResponseEntity<ServiceResponse<List<Loan>>> dashboardBiblioLoanRetards(){
        try {
            return loanService.dashboardBiblioLoanRetards();
        } catch (RuntimeException e){
            ServiceResponse<List<Loan>> response = new ServiceResponse<>("LOAD_LOAN_FAILED", "{loan.load-fail}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }
}
