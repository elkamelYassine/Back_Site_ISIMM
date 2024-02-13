package tn.isimm.manager.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.isimm.manager.domain.Administrateur;

/**
 * Spring Data JPA repository for the Administrateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdministrateurRepository extends JpaRepository<Administrateur, Long> {}
