package fr.eni.bookhub_back.loan;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository extends JpaRepository<Loan, Integer> {
}
