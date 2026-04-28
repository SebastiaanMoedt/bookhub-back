package fr.eni.bookhub_back.dashboard;

import fr.eni.bookhub_back.book.BookRepository;
import fr.eni.bookhub_back.book.BookRestController;
import fr.eni.bookhub_back.book.BookService;
import fr.eni.bookhub_back.common.ServiceResponse;
import fr.eni.bookhub_back.loan.Loan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DashboardService {

    private final static Logger logger = LoggerFactory.getLogger(BookRestController.class);

    private final BookRepository bookRepository;

    public DashboardService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public ResponseEntity<ServiceResponse<DashboardDTO>> getAllInfo() {
        DashboardDTO dto = new DashboardDTO();
        dto.setMostRead(bookRepository.dashboardMostReadBooks());
        //TODO: START HERE SEB
    }
}
