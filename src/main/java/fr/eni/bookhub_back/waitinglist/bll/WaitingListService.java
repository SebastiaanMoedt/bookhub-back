package fr.eni.bookhub_back.waitinglist.bll;

import fr.eni.bookhub_back.book.Book;
import fr.eni.bookhub_back.book.BookService;
import fr.eni.bookhub_back.common.ServiceResponse;
import fr.eni.bookhub_back.locale.LocaleHelper;
import fr.eni.bookhub_back.user.bll.UserService;
import fr.eni.bookhub_back.user.bo.User;
import fr.eni.bookhub_back.waitinglist.StatusReservation;
import fr.eni.bookhub_back.waitinglist.WaitingList;
import fr.eni.bookhub_back.waitinglist.dao.IWaitingListDao;
import fr.eni.bookhub_back.waitinglist.dto.ReservationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WaitingListService {

    private final BookService bookService ;
    private final UserService userService ;
    private final IWaitingListDao waitingListDao ;
    private final LocaleHelper lH;
    private final static Logger logger = LoggerFactory.getLogger(WaitingList.class);

    public WaitingListService(BookService bookService, UserService userService, IWaitingListDao waitingListDao, LocaleHelper lH) {
        this.bookService = bookService;
        this.userService = userService;
        this.waitingListDao = waitingListDao;
        this.lH = lH;
    }

    public ResponseEntity<ServiceResponse<List<WaitingList>>> dashboardUserWaitingListPending(Integer userId){
        try {
            List<WaitingList> books = waitingListDao.dashboardUserWaitingListPending(userId);
            ServiceResponse<List<WaitingList>> response = new ServiceResponse<>("LOAD_WAITING_SUCCESS", lH.i18n("waitinglist.load-success"), books);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            ServiceResponse<List<WaitingList>> response = new ServiceResponse<>("LOAD_WAITING_FAIL", lH.i18n("waitinglist.load-failed-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

    public ResponseEntity<ServiceResponse<ReservationDto>> addReservation(Integer bookToResa, Integer userId){
        try {
            Book book = bookService.findBookById(bookToResa).getBody().data;
            User user = userService.findById(userId);
            LocalDateTime dateResa = LocalDateTime.now();
            WaitingList resa = WaitingList.builder()
                    .book(book)
                    .user(user)
                    .dateAdded(dateResa)
                    .status(StatusReservation.PENDING)
                    .rank(null)
                    .build();

            WaitingList resaDBB = waitingListDao.save(resa);

            // on construit l'objet à retourner ReservationDto
            ReservationDto resaToJson = ReservationDto.builder()
                    .id(resaDBB.getId())
                    .userId(resaDBB.getUser().getId())
                    .bookId(resaDBB.getBook().getId())
                    .dateResa(resaDBB.getDateAdded())
                    .title(resaDBB.getBook().getTitle())
                    .rank(resaDBB.getRank())
                    .status(resaDBB.getStatus())
                    .build();

            ServiceResponse<ReservationDto> response = new ServiceResponse<>("RESA_SAVED", lH.i18n("resa.saved-success"), resaToJson);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            ServiceResponse<ReservationDto> response = new ServiceResponse<>("RESA_SAVE_FAILED", lH.i18n("resa.save-failed-error"));
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(response);
        }
    }

//    public ResponseEntity<ServiceResponse<WaitingList>> addToWishlist(ReservationDto wishDto){
//        // conversion dto to BO
//        WaitingList wishToAdd = new WaitingList();
//        Book book = bookService.findBookById(wishDto.getBookId()).getBody().data;
//        User user = userService.findById(wishDto.getUserId());
//        LocalDateTime date = wishDto.getDateResa() ;
//        wishToAdd.setBook(book);
//        wishToAdd.setUser(user);
//        wishToAdd.setDateAdded(date);
//        // save
//        WaitingList wishDBB = waitingListDao.save(wishToAdd) ;
//        ServiceResponse<WaitingList> response = new ServiceResponse<>("RESA_SAVED", lH.i18n("resa.saved-success"), wishDBB);
//        return ResponseEntity.status(HttpStatus.CREATED).body(response);
//    }
}
