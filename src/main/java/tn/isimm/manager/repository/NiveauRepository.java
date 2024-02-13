package tn.isimm.manager.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.isimm.manager.domain.Niveau;

/**
 * Spring Data JPA repository for the Niveau entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NiveauRepository extends JpaRepository<Niveau, Long> {}
