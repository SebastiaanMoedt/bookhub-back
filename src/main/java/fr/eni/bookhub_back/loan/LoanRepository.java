package fr.eni.bookhub_back.loan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Integer> {

    // Dashboard Lecteur : Emprunts en cours
    @Query(value = "SELECT * FROM Dashboard_Loan_Ongoing WHERE user_id=:userId", nativeQuery = true)
    List<Loan> Dashboard_User_Loan_Ongoing(@Param("userId") int userId);

    // Dashboard Bibliothécaire : Emprunts actifs
    @Query(value = "SELECT * FROM Dashboard_Loan_Ongoing", nativeQuery = true)
    List<Loan> Dashboard_Biblio_Loan_Ongoing();

    // Dashboard Lecteur : Retards
    @Query(value = "SELECT * FROM Dashboard_Loan_Retards WHERE user_id=:userId", nativeQuery = true)
    List<Loan> Dashboard_User_Loan_Retards(@Param("userId") int userId);

    // Dashboard Bibliothécaire : Retards
    @Query(value = "SELECT * FROM Dashboard_Loan_Retards", nativeQuery = true)
    List<Loan> Dashboard_Biblio_Loan_Retards();
}
