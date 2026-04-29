package fr.eni.bookhub_back.waitinglist.dao.jpa;

import fr.eni.bookhub_back.waitinglist.WaitingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WaitingListJpaRepository extends JpaRepository<WaitingList, Integer> {
    // --- inutile d'ajouter la méthode save() car Spring Data JPA s'occupe déjà de l'implémentation ---

    // Dashboard Lecteur: Réservations
    @Query(value = "SELECT * FROM Dashboard_WaitingList_Pending WHERE user_id=:userId", nativeQuery = true)
    List<WaitingList> dashboardUserWaitingListPending(@Param("userId") int userId);

    @Query(value = "SELECT COUNT(*) AS nbResaEnCours FROM waiting_list WHERE user_id=:userId AND status_reservation='PENDING'", nativeQuery = true)
    Integer nbResaEnCoursByUser(int userId);
}
