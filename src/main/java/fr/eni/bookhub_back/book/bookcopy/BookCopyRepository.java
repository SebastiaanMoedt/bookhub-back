package fr.eni.bookhub_back.book.bookcopy;

import fr.eni.bookhub_back.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookCopyRepository extends JpaRepository<BookCopy, Integer> {
    List<BookCopy> findAllByBook(Book book);

    // requête JPQL
    @Modifying
    @Query("DELETE FROM BookCopy bc WHERE bc.id = :id")
    void deleteCopyById(@Param("id") int id);
}