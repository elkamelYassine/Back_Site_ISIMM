package tn.isimm.manager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.isimm.manager.domain.Administrateur;

/**
 * Spring Data JPA repository for the Administrateur entity.
 */
@Repository
public interface AdministrateurRepository extends JpaRepository<Administrateur, Long> {
    default Optional<Administrateur> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Administrateur> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Administrateur> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select administrateur from Administrateur administrateur left join fetch administrateur.user",
        countQuery = "select count(administrateur) from Administrateur administrateur"
    )
    Page<Administrateur> findAllWithToOneRelationships(Pageable pageable);

    @Query("select administrateur from Administrateur administrateur left join fetch administrateur.user")
    List<Administrateur> findAllWithToOneRelationships();

    @Query("select administrateur from Administrateur administrateur left join fetch administrateur.user where administrateur.id =:id")
    Optional<Administrateur> findOneWithToOneRelationships(@Param("id") Long id);
}
