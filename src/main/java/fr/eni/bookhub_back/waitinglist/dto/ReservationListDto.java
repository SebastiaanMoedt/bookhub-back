package fr.eni.bookhub_back.waitinglist.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"userId", "bookId"})
public class ReservationListDto {
    private int userId ;
    private int bookId ;
    private List<ReservationDto> reservations;
    private int nbResa;
}