package fr.eni.bookhub_back.book;

import fr.eni.bookhub_back.common.ServiceResponse;
import fr.eni.bookhub_back.locale.LocaleHelper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class BookService {

    private BookRepository bookRepository;
    private LocaleHelper localeHelper;

    private final static Logger logger = LoggerFactory.getLogger(BookService.class);

    public ResponseEntity<?> findBooks(int page, int size, String sortBy) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
            Page<Book> pageBook = bookRepository.findAll(pageable);
            ServiceResponse<?> response =
                    new ServiceResponse<>("BOOKS_FOUND", "Livres trouvés", pageBook);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ServiceResponse<Page<Book>> response =
                    new ServiceResponse<>("BOOKS_NOT_FOUND", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public Optional<Book> findBookObjectByISBN(String isbn) {
        return bookRepository.findBookByIsbn(isbn);

    }

    public ResponseEntity<ServiceResponse<Book>> findBookByISBN(String isbn) {
        try {
            Book book = bookRepository.findBookByIsbn(isbn).get();
            ServiceResponse<Book> response =
                    new ServiceResponse<>("BOOK_FOUND", "Livre trouvé", book);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ServiceResponse<Book> response =
                    new ServiceResponse<>("BOOK_NOT_FOUND", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public ResponseEntity<ServiceResponse<Book>> createBook(Book b) {
        if (b == null) {
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_NULL", localeHelper.i18n("book.null-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        if (b.getIsbn() == null || b.getIsbn().isBlank()) {
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_ISBN_BLANK", localeHelper.i18n("book.isbn.blank-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        if (b.getTitle() == null || b.getTitle().isBlank()) {
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_TITLE_BLANK", localeHelper.i18n("book.title.blank-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        if (b.getAuthor() == null || b.getAuthor().isBlank()) {
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_AUTHOR_BLANK", localeHelper.i18n("book.author.blank-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        Optional<Book> optBook = bookRepository.findBookByIsbn(b.getIsbn());

        if (optBook.isPresent()) {
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_ALREADY_EXISTS", localeHelper.i18n("book.isbn-already-exists-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        try {
            bookRepository.save(b);
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_CREATE_SUCCESS", localeHelper.i18n("book.create-success"));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_CREATION_FAILED", localeHelper.i18n("book.creation-failed"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    public ResponseEntity<ServiceResponse<Book>> updateBook(int id, Book b) {
        // Vérifier qu'on reçoit bien un livre en body
        if (b == null) {
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_NULL", localeHelper.i18n("book.null-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        // Vérifier si l'id passé en param correspond bien à un livre en DB
        Optional<Book> bookInDB = bookRepository.findById(id);

        // Si non, erreur: livre inexistant
        if (bookInDB.isEmpty()) {
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_ID_DOESNT_EXIST", localeHelper.i18n("book.id-doesnt-exist-error"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Vérifier qu'on a bien un ISBN dans le body
        if (b.getIsbn() == null || b.getIsbn().isBlank()) {
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_ISBN_BLANK", localeHelper.i18n("book.isbn.blank-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        // Vérifier si l'ISBN a été modifié
        boolean isISBNModified = !bookInDB.get().getIsbn().trim().equals(b.getIsbn().trim());

        // Vérifier si le nouvel ISBN correspond à un livre déjà existant
        // Si oui, erreur
        // Si non, mettre à jour l'ISBN
        if (isISBNModified) {
            Optional<Book> optBookWithNewISBN = bookRepository.findBookByIsbn(b.getIsbn().trim());
            if (optBookWithNewISBN.isPresent()) {
                ServiceResponse<Book> response = new ServiceResponse<>("BOOK_ALREADY_EXISTS", localeHelper.i18n("book.isbn-already-exists-error"));
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
            }
            bookInDB.get().setIsbn(b.getIsbn().trim());
        }

        // Vérifier que le titre est renseigné
        if (b.getTitle() == null || b.getTitle().isBlank()) {
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_TITLE_BLANK", localeHelper.i18n("book.title.blank-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        // Vérifier que l'auteur est renseigné
        if (b.getAuthor() == null || b.getAuthor().isBlank()) {
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_AUTHOR_BLANK", localeHelper.i18n("book.author.blank-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        // Mettre à jour les données
        bookInDB.get().setTitle(b.getTitle().trim());
        bookInDB.get().setAuthor(b.getAuthor().trim());
        bookInDB.get().setCategory(b.getCategory());

        if (b.getDescription() != null && !b.getDescription().trim().isBlank()) {
            bookInDB.get().setDescription(b.getDescription().trim());
        }

        if (b.getCoverUrl() != null && !b.getCoverUrl().trim().isBlank()) {
            bookInDB.get().setCoverUrl(b.getCoverUrl().trim());
        }

        // Modifier l'état ?
        // Modifier la disponibilité ?

        try {
            bookRepository.save(bookInDB.get());
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_UPDATE_SUCCESS", localeHelper.i18n("book.update-success"), b);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            logger.error("BookService updateBook() error : {}", e.getMessage());
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_UPDATE_FAILED", localeHelper.i18n("book.update-failed"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }
}
