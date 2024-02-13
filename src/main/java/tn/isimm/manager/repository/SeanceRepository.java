package tn.isimm.manager.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.isimm.manager.domain.Seance;

/**
 * Spring Data JPA repository for the Seance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {}
