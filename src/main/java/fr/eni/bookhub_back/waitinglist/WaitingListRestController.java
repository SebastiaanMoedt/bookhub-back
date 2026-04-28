package fr.eni.bookhub_back.waitinglist;
import fr.eni.bookhub_back.common.ServiceResponse;
import fr.eni.bookhub_back.loan.Loan;
import fr.eni.bookhub_back.waitinglist.bll.WaitingListService;
import fr.eni.bookhub_back.waitinglist.dto.ReservationDto;
import fr.eni.bookhub_back.waitinglist.dto.ReservationRequestDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class WaitingListRestController {

    private final WaitingListService waitingListService ;

    public WaitingListRestController(WaitingListService waitingListService) {
        this.waitingListService = waitingListService;
    }

//    @GetMapping("/reservations/my")
//    public ResponseEntity<ServiceResponse<List<WaitingList>>> getWishListOfCurrentUser(@Valid @RequestBody int userId) {
//        return waitingListService.getWishListOfCurrentUser(userId);
//    }

    @GetMapping("api/dashboard/dashboardUserWaitingListPending")
    public ResponseEntity<ServiceResponse<List<WaitingList>>> dashboardUserLoanRetards(@RequestParam Integer userId){
        try {
            return waitingListService.dashboardUserWaitingListPending(userId);
        } catch (RuntimeException e){
            ServiceResponse<List<WaitingList>> response = new ServiceResponse<>("LOAD_WAITING_FAILED", "{waitinglist.load-fail}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    @PostMapping("/reservations")
    public ResponseEntity<ServiceResponse<ReservationDto>> addReservation(@RequestParam ReservationRequestDto request) {
        try {
            Integer idBookToResa = request.getBookId();
            Integer idUser = request.getUserId();
            return waitingListService.addReservation(idBookToResa, idUser);
        } catch (RuntimeException e){
            ServiceResponse<ReservationDto> response = new ServiceResponse<>("RESA_SAVE_FAILED", "{resa.save-failed-error}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }
}