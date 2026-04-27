package fr.eni.bookhub_back.waitinglist.dao.jpa;

import fr.eni.bookhub_back.waitinglist.WaitingList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaitingListJpaRepository extends JpaRepository<WaitingList, Integer> {
    // --- inutile d'ajouter la méthode save() car Spring Data JPA s'occupe déjà de l'implémentation ---
}
