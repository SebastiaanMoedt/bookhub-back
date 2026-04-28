package fr.eni.bookhub_back.waitinglist.dao;

import fr.eni.bookhub_back.waitinglist.WaitingList;

import java.util.List;

public interface IWaitingListDao {
    WaitingList save(WaitingList waitingList);
    List<WaitingList> dashboardUserWaitingListPending(int userId);
}
