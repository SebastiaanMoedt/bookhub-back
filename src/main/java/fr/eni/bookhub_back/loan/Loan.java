package fr.eni.bookhub_back.loan;

import fr.eni.bookhub_back.book.bookcopy.BookCopy;
import fr.eni.bookhub_back.user.bo.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "LOAN")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "COPY_ID", nullable = false, referencedColumnName = "id")
    private BookCopy bookcopy;

    @Column(name = "DATE_LOANED", nullable = false)
    private LocalDate dateLoaned;

    @Column(name = "DATE_RETURNED")
    private LocalDate dateReturned;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "id")
    private User user;
}
