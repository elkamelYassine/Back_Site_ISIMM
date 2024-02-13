package tn.isimm.manager.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.isimm.manager.domain.Actulaite;

/**
 * Spring Data JPA repository for the Actulaite entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActulaiteRepository extends JpaRepository<Actulaite, Long> {}
