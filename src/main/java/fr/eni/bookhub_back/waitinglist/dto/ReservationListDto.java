package fr.eni.bookhub_back.waitinglist.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationListDto {
    private List<ReservationDto> reservations;
    private int nbResa;
}