package fr.eni.bookhub_back.waitinglist;
import fr.eni.bookhub_back.common.ServiceResponse;
import fr.eni.bookhub_back.loan.Loan;
import fr.eni.bookhub_back.waitinglist.bll.WaitingListService;
import fr.eni.bookhub_back.waitinglist.dto.ReservationDto;
import jakarta.validation.Valid;
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

    @PostMapping("/reservations")
    public ResponseEntity<ServiceResponse<WaitingList>> addToWishlist(@Valid @RequestBody ReservationDto wishDto) {
        try {
            return waitingListService.addToWishlist(wishDto);
        } catch (RuntimeException e){
            ServiceResponse<WaitingList> response = new ServiceResponse<>("RESA_SAVE_FAILED", "{resa.save-failed-error}");
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }
}