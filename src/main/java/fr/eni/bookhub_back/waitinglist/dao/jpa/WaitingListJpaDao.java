package fr.eni.bookhub_back.waitinglist.dao.jpa;

import fr.eni.bookhub_back.waitinglist.WaitingList;
import fr.eni.bookhub_back.waitinglist.dao.IWaitingListDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class WaitingListJpaDao implements IWaitingListDao {

    private final WaitingListJpaRepository waitingListRepository;

    public WaitingListJpaDao(WaitingListJpaRepository waitingListRepository) {
        this.waitingListRepository = waitingListRepository;
    }

    @Override
    public WaitingList save(WaitingList waitingList) {
        return waitingListRepository.save(waitingList);
    }

    @Override
    public List<WaitingList> dashboardUserWaitingListPending(int userId) {
        return waitingListRepository.dashboardUserWaitingListPending(userId);
    }
}
