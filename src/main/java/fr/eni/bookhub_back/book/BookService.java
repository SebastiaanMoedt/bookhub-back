package fr.eni.bookhub_back.book;

import fr.eni.bookhub_back.common.ServiceResponse;
import fr.eni.bookhub_back.review.Review;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
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

@AllArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository; // pas besoin de l'injecter par constructeur, Lombock s'en occupe

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
                        availability.equals("NOW") ? true : false);
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
                    new ServiceResponse<>("BOOKS_FOUND", "{book.found}", pageBook);
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
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_NULL", "{book.null-error}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }

        // @TODO code qui générait des erreurs, j'ai mis entre () en attendant le fix
//        if (b.getTitle() == null || b.getTitle().isBlank()) {
//            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_TITLE_BLANK", "{book.title.blank-error}");
//            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
//        }
//
//        if (b.getAuthor() == null || b.getAuthor().isBlank()) {
//            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_AUTHOR_BLANK", "{book.author.blank-error}");
//            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
//        }
//
//        Optional<Book> optBook = bookRepository.findBookByIsbn(b.getIsbn());
//
//        if (optBook.isPresent()) {
//            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_ALREADY_EXISTS", "{book.isbn-already-exists-error}");
//            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
//        }

        try {
            bookRepository.save(b);
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_CREATE_SUCCESS", "{book.create-success}", b);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            ServiceResponse<Book> response = new ServiceResponse<>("BOOK_SAVE_FAILED", "{book.save-failed-error}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }
}
