package fr.eni.bookhub_back.book;

import fr.eni.bookhub_back.loan.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {
    // Avec @EntityGraph, Hibernate charge tout en une seule requête :
    @EntityGraph(attributePaths = {"copies", "reviews", "category", "reservations"})
    Page<Book> findAll(Specification<Book> spec, Pageable pageable); // ← Specification vient de JpaSpecificationExecutor

    Optional<Book> findBookByIsbn(String isbn);

    // Top 10 Livres les + empruntés
    @Query(value = "SELECT TOP 10 * FROM Dashboard_Most_Read_Books ORDER BY nbLoans DESC", nativeQuery = true)
    List<Book> dashboardMostReadBooks();

    // Total livres
    @Query(value = "SELECT * FROM Dashboard_NbTotal_Book", nativeQuery = true)
    Integer dashboardNbTotalBook();

}