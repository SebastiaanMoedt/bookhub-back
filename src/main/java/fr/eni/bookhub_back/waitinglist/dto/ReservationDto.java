package fr.eni.bookhub_back.waitinglist.dto;
import fr.eni.bookhub_back.waitinglist.StatusReservation;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id"})
public class ReservationDto {
    private int id ;
    private int userId ;
    private int bookId ;
    private String bookTitle ;
    private LocalDateTime dateResa ;
    private int rank ; // rang dans la liste d'attente
    private StatusReservation status ;
}