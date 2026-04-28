package fr.eni.bookhub_back.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {
    // Avec @EntityGraph, Hibernate charge tout en une seule requête :
    @EntityGraph(attributePaths = {"copies", "reviews", "category", "reservations"})
    Page<Book> findAll(Specification<Book> spec, Pageable pageable); // ← Specification vient de JpaSpecificationExecutor

    Optional<Book> findBookByIsbn(String isbn);
}