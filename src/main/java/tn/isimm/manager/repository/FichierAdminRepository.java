package tn.isimm.manager.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.isimm.manager.domain.FichierAdmin;

/**
 * Spring Data JPA repository for the FichierAdmin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FichierAdminRepository extends JpaRepository<FichierAdmin, Long> {}
