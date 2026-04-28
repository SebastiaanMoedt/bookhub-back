package fr.eni.bookhub_back.dashboard;

import fr.eni.bookhub_back.book.BookService;
import fr.eni.bookhub_back.common.ServiceResponse;
import fr.eni.bookhub_back.loan.Loan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {


    private final BookService bookService;

    public DashboardService(BookService bookService) {
        this.bookService = bookService;
    }

    public ResponseEntity<ServiceResponse<DashboardDTO>> getAllInfo() {
        try {
            DashboardDTO dto = new DashboardDTO();
            dto.setMostRead(bookService.dashboardMostReadBooks());
            ServiceResponse<List<Loan>> response = new ServiceResponse<>("LOAD_LOAN_SUCCESS", localeHelper.i18n("loan.load-success"), loans);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            ServiceResponse<List<Loan>> response = new ServiceResponse<>("LOAD_LOAN_FAIL", localeHelper.i18n("loan.load-failed-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }
}
