package fr.eni.bookhub_back.loan;
import fr.eni.bookhub_back.book.Book;
import fr.eni.bookhub_back.book.bookcopy.BookCopy;
import fr.eni.bookhub_back.common.ServiceResponse;
import fr.eni.bookhub_back.locale.LocaleHelper;
import lombok.AllArgsConstructor;
import fr.eni.bookhub_back.dashboard.DashboardDTO;
import fr.eni.bookhub_back.dashboard.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api")
public class LoanRestController {

    private LocaleHelper localeHelper;
    LoanService loanService;
    private DashboardService dashboardService;

    @PostMapping("/books/loan")
    public ResponseEntity<ServiceResponse<Loan>> reserverLivre(@RequestBody LoanDTO loanDTO){
        try {
            return loanService.reserverLivre(loanDTO);
        } catch (RuntimeException e){
            ServiceResponse<Loan> response = new ServiceResponse<>("LOAN_SAVE_FAILED", "{loan.save-failed-error}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    @GetMapping("/dashboard/dashboardUserLoanOngoing")
    public ResponseEntity<ServiceResponse<List<Loan>>> dashboardUserLoanOngoing(@RequestParam Integer userId){
        try {
            return loanService.dashboardUserLoanOngoing(userId);
        } catch (RuntimeException e){
            ServiceResponse<List<Loan>> response = new ServiceResponse<>("LOAD_LOAN_FAILED", "{loan.load-fail}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    @GetMapping("/dashboard/dashboardUserBookLoanReadByUser")
    public ResponseEntity<ServiceResponse<List<Loan>>> dashboardUserBookLoanReadByUser(@RequestParam Integer userId){
        try {
            return loanService.dashboardUserBookLoanReadByUser(userId);
        } catch (RuntimeException e){
            ServiceResponse<List<Loan>> response = new ServiceResponse<>("LOAD_LOAN_FAILED", "{loan.load-fail}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    @GetMapping("/dashboard/dashboardBiblioLoanOngoing")
    public ResponseEntity<ServiceResponse<List<Loan>>> dashboardBiblioLoanOngoing(){
        try {
            return loanService.dashboardBiblioLoanOngoing();
        } catch (RuntimeException e){
            ServiceResponse<List<Loan>> response = new ServiceResponse<>("LOAD_LOAN_FAILED", "{loan.load-fail}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    @GetMapping("/dashboard/dashboardUserLoanRetards")
    public ResponseEntity<ServiceResponse<List<Loan>>> dashboardUserLoanRetards(@RequestParam Integer userId){
        try {
            return loanService.dashboardUserLoanRetards(userId);
        } catch (RuntimeException e){
            ServiceResponse<List<Loan>> response = new ServiceResponse<>("LOAD_LOAN_FAILED", "{loan.load-fail}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    @GetMapping("/dashboard/loadAllBiblio")
    public ResponseEntity<ServiceResponse<DashboardDTO>> dashboardLoadAllBiblio(){
        try {
            return dashboardService.getAllInfo();
        } catch (RuntimeException e){
            ServiceResponse<DashboardDTO> response = new ServiceResponse<>("LOAD_LOAN_FAILED", "{dashboard.load-fail}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    @GetMapping("/dashboard/dashboardBiblioLoanRetards")
    public ResponseEntity<ServiceResponse<List<Loan>>> dashboardBiblioLoanRetards(){
        try {
            return loanService.dashboardBiblioLoanRetards();
        } catch (RuntimeException e){
            ServiceResponse<List<Loan>> response = new ServiceResponse<>("LOAD_LOAN_FAILED", "{loan.load-fail}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    @PostMapping("loans/return")
    public ResponseEntity<ServiceResponse<Loan>> returnBook(Loan loan, BookCopy bookCopy) {
        try {
            return loanService.returnBook(loan, bookCopy);
        } catch (RuntimeException e){
            ServiceResponse<Loan> response = new ServiceResponse<>("RETURN_LOAN_FAILED", localeHelper.i18n("loan.return-failed"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }
}
