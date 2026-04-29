package fr.eni.bookhub_back.dashboard;

import fr.eni.bookhub_back.book.BookRepository;
import fr.eni.bookhub_back.book.BookRestController;
import fr.eni.bookhub_back.book.BookService;
import fr.eni.bookhub_back.common.ServiceResponse;
import fr.eni.bookhub_back.loan.Loan;
import fr.eni.bookhub_back.loan.LoanRepository;
import fr.eni.bookhub_back.locale.LocaleHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardService {

    private final static Logger logger = LoggerFactory.getLogger(BookRestController.class);

    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    @Autowired
    private LocaleHelper localeHelper;

    public DashboardService(BookRepository bookRepository, LoanRepository loanRepository) {
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
    }

    public ResponseEntity<ServiceResponse<DashboardDTO>> getAllInfo() {
        try {
            DashboardDTO dto = new DashboardDTO();
            dto.setMostRead(bookRepository.dashboardMostReadBooks());

            dto.setBookCount(bookRepository.dashboardNbTotalBook());

            List<Loan> lateLoans = loanRepository.dashboardBiblioLoanRetards();
            List<DashboardLoanDTO> lateloanDTOList = new ArrayList<>();
            for (Loan loan : lateLoans) {
                DashboardLoanDTO dashLoanDTO = new DashboardLoanDTO();
                dashLoanDTO.setBookTitle(loan.getBookcopy().getBook().getTitle());
                dashLoanDTO.setDateLoaned(loan.getDateLoaned());
                dashLoanDTO.setUser(loan.getUser());
                lateloanDTOList.add(dashLoanDTO);
            }

            dto.setLateLoans(lateloanDTOList);

            List<Loan> openLoans = loanRepository.dashboardBiblioLoanOngoing();
            List<DashboardLoanDTO> openLoanDTOList = new ArrayList<>();
            for (Loan loan : openLoans) {
                DashboardLoanDTO dashLoanDTO = new DashboardLoanDTO();
                dashLoanDTO.setBookTitle(loan.getBookcopy().getBook().getTitle());
                dashLoanDTO.setDateLoaned(loan.getDateLoaned());
                dashLoanDTO.setUser(loan.getUser());
                openLoanDTOList.add(dashLoanDTO);
            }
            dto.setOpenLoans(openLoanDTOList);

            ServiceResponse<DashboardDTO> response = new ServiceResponse<>("LOAD_DASHBOARD_SUCCESS", localeHelper.i18n("dashboard.load-success"), dto);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            ServiceResponse<DashboardDTO> response = new ServiceResponse<>("LOAD_DASHBOARD_FAIL", localeHelper.i18n("dashboard.load-fail"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }
}
