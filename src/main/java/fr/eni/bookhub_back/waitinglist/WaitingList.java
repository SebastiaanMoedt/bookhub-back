package fr.eni.bookhub_back.waitinglist;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.eni.bookhub_back.book.Book;
import fr.eni.bookhub_back.user.bo.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "WAITING_LIST")
public class WaitingList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "id")
    private User user;

    @Column(name = "DATE_ADDED")
    private LocalDateTime dateAdded;

    @ManyToOne
    @JoinColumn(name = "BOOK_ID", nullable = false, referencedColumnName = "id")
    @ToString.Exclude        // ← évite book → copies → book → copies...
    @EqualsAndHashCode.Exclude
    @JsonIgnore
    private Book book;

    @Column(name = "RANK_NUMBER")
    private Integer rank;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_RESERVATION")
    private StatusReservation status;
}
