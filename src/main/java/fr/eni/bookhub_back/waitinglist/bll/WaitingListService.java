package fr.eni.bookhub_back.waitinglist.bll;

import fr.eni.bookhub_back.book.Book;
import fr.eni.bookhub_back.book.BookService;
import fr.eni.bookhub_back.common.ServiceResponse;
import fr.eni.bookhub_back.loan.Loan;
import fr.eni.bookhub_back.locale.LocaleHelper;
import fr.eni.bookhub_back.user.bll.UserService;
import fr.eni.bookhub_back.user.bo.User;
import fr.eni.bookhub_back.waitinglist.WaitingList;
import fr.eni.bookhub_back.waitinglist.dao.IWaitingListDao;
import fr.eni.bookhub_back.waitinglist.dto.ReservationDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class WaitingListService {

    private final BookService bookService ;
    private final UserService userService ;
    private final IWaitingListDao waitingListDao ;
    private final LocaleHelper lH;

    public WaitingListService(BookService bookService, UserService userService, IWaitingListDao waitingListDao, LocaleHelper lH) {
        this.bookService = bookService;
        this.userService = userService;
        this.waitingListDao = waitingListDao;
        this.lH = lH;
    }

    public ResponseEntity<ServiceResponse<WaitingList>> getWishListOfCurrentUser(int userId){
        return null ;
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
