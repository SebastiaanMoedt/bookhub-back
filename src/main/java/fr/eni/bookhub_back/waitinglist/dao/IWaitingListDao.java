package fr.eni.bookhub_back.waitinglist.dao;

import fr.eni.bookhub_back.waitinglist.WaitingList;

public interface IWaitingListDao {
    WaitingList save(WaitingList waitingList);
}
