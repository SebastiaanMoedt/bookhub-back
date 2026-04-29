package fr.eni.bookhub_back.dashboard;

import fr.eni.bookhub_back.book.Book;
import fr.eni.bookhub_back.loan.Loan;
import fr.eni.bookhub_back.loan.LoanDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {

    int bookCount;
    List<DashboardLoanDTO> lateLoans;
    List<DashboardLoanDTO> openLoans;
    List<Book> mostRead;
}
