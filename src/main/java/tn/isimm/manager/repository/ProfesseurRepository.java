package tn.isimm.manager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.isimm.manager.domain.Professeur;

/**
 * Spring Data JPA repository for the Professeur entity.
 *
 * When extending this class, extend ProfesseurRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ProfesseurRepository extends ProfesseurRepositoryWithBagRelationships, JpaRepository<Professeur, Long> {
    default Optional<Professeur> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Professeur> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Professeur> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select professeur from Professeur professeur left join fetch professeur.user",
        countQuery = "select count(professeur) from Professeur professeur"
    )
    Page<Professeur> findAllWithToOneRelationships(Pageable pageable);

    @Query("select professeur from Professeur professeur left join fetch professeur.user")
    List<Professeur> findAllWithToOneRelationships();

    @Query("select professeur from Professeur professeur left join fetch professeur.user where professeur.id =:id")
    Optional<Professeur> findOneWithToOneRelationships(@Param("id") Long id);
}
