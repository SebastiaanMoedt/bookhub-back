package fr.eni.bookhub_back.waitinglist;

import fr.eni.bookhub_back.book.Book;
import fr.eni.bookhub_back.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "WAITING_LIST")
public class WaitingList {

    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "USER_ID", referencedColumnName = "id")
    private User user;

    @Column(name = "DATE_ADDED")
    private Date dateAdded;

    @ManyToOne
    @JoinColumn(name = "isbn")
    private Book book;
}
