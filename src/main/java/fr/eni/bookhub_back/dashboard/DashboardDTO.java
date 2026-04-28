package fr.eni.bookhub_back.dashboard;

import fr.eni.bookhub_back.book.Book;
import fr.eni.bookhub_back.loan.Loan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {

    int bookCount;
    List<Loan> lateLoans;
    List<Loan> openLoans;
    List<Book> mostRead;
}
