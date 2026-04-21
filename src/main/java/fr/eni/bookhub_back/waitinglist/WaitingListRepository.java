package fr.eni.bookhub_back.waitinglist;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingListRepository extends JpaRepository<WaitingList, Integer> {
}
