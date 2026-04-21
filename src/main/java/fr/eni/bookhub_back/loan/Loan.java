package fr.eni.bookhub_back.loan;

import fr.eni.bookhub_back.book.bookcopy.BookCopy;
import fr.eni.bookhub_back.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToOne
    @JoinColumn(name = "COPY_ID", nullable = false)
    private BookCopy bookcopy;

    @Column(name = "DATE_LOANED", nullable = false)
    private Date dateLoaned;

    @Column(name = "DATE_RETURNED")
    private Date dateReturned;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    private User user;
}
