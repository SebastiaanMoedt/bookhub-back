package fr.eni.bookhub_back.book;

import fr.eni.bookhub_back.book.bookcopy.BookCopy;
import fr.eni.bookhub_back.book.bookcopy.BookCopyRepository;
import fr.eni.bookhub_back.book.bookcopy.BookState;
import fr.eni.bookhub_back.common.ServiceResponse;
import fr.eni.bookhub_back.review.Review;
import fr.eni.bookhub_back.user.dao.jpa.UserJpaRepository;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import fr.eni.bookhub_back.locale.LocaleHelper;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

@AllArgsConstructor
@Service
public class BookService {

    private final UserJpaRepository userJpaRepository;
    private LocaleHelper localeHelper;
    private BookRepository bookRepository;
    private BookCopyRepository bookCopyRepository;

    private final static Logger logger = LoggerFactory.getLogger(BookService.class);

    public ResponseEntity<?> findBooks(int page, int size, String sortBy, String search, List<String> categories, String availability) {

        Specification<Book> spec = Specification.allOf(); // on prend toutes les lignes au départ !

        // on crée dynamiquement nos filtres de recherche
        // METHODE : Criteria API avec des Specifications — solution puissante pour les req dynamiques et combinables.

        // Filtre recherche textuelle multi-mots
        if (search != null && !search.isBlank()) {
            String[] mots = search.trim().split("\\s+");
            for (String mot : mots) {
                String pattern = "%" + mot + "%";
                spec = spec.or(
                        // ajout d'une nouvelle condition via un callback (root, query, cb) -> ...
                        // où : root = la table Book, query = la req entière, cb (criteria builder) = l'outil pour construire les conditions
                        (root, query, cb) ->
                                cb.or(
                                        cb.like(root.get("author"), pattern),
                                        cb.like(root.get("title"), pattern),
                                        cb.like(root.get("isbn"), pattern)
                                )
                );
            }
        }
        // Filtre catégories
        if (categories != null && !categories.contains("ALL")) {
            spec = spec.and((root, query, cb) ->
                    root.get("category").get("category").in(categories) // On accède au champ "category" de l'objet Category
            );
        }
        // Filtre disponibilité (les catégories sont exclusives)
        if (availability != null && !availability.contains("ALL")) {
            spec = spec.and((root, query, cb) -> {
                query.distinct(true);
                return cb.equal(
                        root.join("copies", JoinType.LEFT).get("isAvailable"),
                        availability.equals("NOW"));
            });
        }

        // on passe par une sous-requête => pour faire un ORDER BY de notre (sub-query)
        if ("BEST_NOTE".equals(sortBy)) {
            spec = spec.and((root, query, cb) -> {
                // Sous-requête qui calcule AVG(score) par livre
                Subquery<Double> subquery = query.subquery(Double.class); // on crée une req qui va créer un double
                Root<Review> reviewRoot = subquery.from(Review.class); // → (SELECT ... FROM review)
                subquery.select(cb.avg(reviewRoot.get("score"))) // → (SELECT AVG(score) FROM review)
                        .where(cb.equal(reviewRoot.get("book"), root)); // → (SELECT AVG(score) FROM review WHERE book_id = b.id)
                // ORDER BY sur le résultat de la sous-requête
                query.orderBy(cb.desc(subquery));
                return cb.conjunction();
            });
        }

        Sort sort = switch (sortBy) {
            case "TITLE_ZTOA"       -> Sort.by(Sort.Direction.DESC, "title");
            case "MOST_RECENT_DATE" -> Sort.by(Sort.Direction.DESC, "dateParution"); // -- @TODO: Modif BDD ajouter la variable dans la BO !
            case "BEST_NOTE"        -> Sort.unsorted(); // ← le tri est déjà dans la Specification
            default                 -> Sort.by(Sort.Direction.ASC,  "title"); // valeur par défaut
        };

        try {
            Pageable pageable = PageRequest.of(page, size, sort);    // permet de dire comment découper et trier les résultats de recherche
            Page<Book> pageBook = bookRepository.findAll(spec, pageable);       // spec = obj qui décrit les filtres de ma req -> construit dynamiquement !
            ServiceResponse<?> response =
                    new ServiceResponse<>("BOOKS_FOUND", localeHelper.i18n("books.found"), pageBook);
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
                    new ServiceResponse<>("BOOK_FOUND", localeHelper.i18n("book.found"), book);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ServiceResponse<Book> response =
                    new ServiceResponse<>("BOOK_NOT_FOUND", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public ResponseEntity<ServiceResponse<Book>> findBookById(int id) {
        try {
            Book book = bookRepository.findById(id).get();
            ServiceResponse<Book> response =
                    new ServiceResponse<>("BOOK_FOUND", localeHelper.i18n("book.found"), book);
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
            // Save new book in base
            bookRepository.save(b);

            // Instanciate new book copy
            BookCopy newBookCopy = new BookCopy();

            // Set values for the new book copy
            newBookCopy.setBook(b);
            newBookCopy.setAvailable(true);
            newBookCopy.setState(BookState.NEW);

            // Save new book copy in base
            bookCopyRepository.save(newBookCopy);

            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_CREATE_SUCCESS", localeHelper.i18n("book.create-success"), b);
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

        // TODO: déplacer dans updateBookCopy
        // Modifier l'état ?
        // Modifier la disponibilité ?

        try {
            bookRepository.save(bookInDB.get());
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_UPDATE_SUCCESS", localeHelper.i18n("book.update-success"), b);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            logger.error("BookService updateBook() error : {}", e.getMessage());
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_UPDATE_FAILED", localeHelper.i18n("book.update-failed-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    public ResponseEntity<ServiceResponse<Book>> deleteBook(int id) {
        Optional<Book> bookInDB = bookRepository.findById(id);
        if (bookInDB.isEmpty()) {
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_ID_DOESNT_EXIST", localeHelper.i18n("book.id-doesnt-exist-error"));
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Boucler sur les copies
        // Vérifier s'il y a des copies en location
            // Si oui, ne pas permettre la suppression
            // Sinon, OK
        Set<BookCopy> copies = bookInDB.get().getCopies();
        Optional<BookCopy> copyCurrentlyInLoan = copies.stream().filter(c -> !c.isAvailable()).findFirst();

        if(copyCurrentlyInLoan.isPresent()) {
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_CANNOT_DELETE_COPY_IN_LOAN", localeHelper.i18n("book.cant-delete-copy-in-loan-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        try {
            bookRepository.deleteById(id);
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_DELETE_SUCCESS", localeHelper.i18n("book.delete-success"));
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            logger.error("BookService deleteBook() error : {}", e.getMessage());
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_DELETE_FAILED", localeHelper.i18n("book.delete-failed-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    //    List<Book> findLoanedBooksByUsername(String username){
//        try {
//            ServiceResponse<List<Book>> response = new ServiceResponse<>("BOOK_UPDATE_SUCCESS", localeHelper.i18n("book.update-success"), b);
//            return ResponseEntity.status(HttpStatus.OK).body(response);
//        } catch (RuntimeException e) {
//            logger.error("BookService updateBook() error : {}", e.getMessage());
//            ServiceResponse<List<Book>> response = new ServiceResponse<>("BOOK_UPDATE_FAILED", localeHelper.i18n("book.update-failed"));
//            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
//        }
//    }

}
