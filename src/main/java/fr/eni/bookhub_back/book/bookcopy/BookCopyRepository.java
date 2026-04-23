package fr.eni.bookhub_back.book.bookcopy;

import fr.eni.bookhub_back.book.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookCopyRepository extends JpaRepository<BookCopy, Integer> {
    List<BookCopy> findAllByBook(Book book);
}
