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
    private Integer id ;
    private Integer userId ;
    private Integer bookId ;
    private String title;
    private LocalDateTime dateResa ;
    private Integer rank ; // rang dans la liste d'attente
    private StatusReservation status ;
}