package tn.isimm.manager.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import tn.isimm.manager.domain.Etudiant;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class EtudiantRepositoryWithBagRelationshipsImpl implements EtudiantRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Etudiant> fetchBagRelationships(Optional<Etudiant> etudiant) {
        return etudiant.map(this::fetchClubs);
    }

    @Override
    public Page<Etudiant> fetchBagRelationships(Page<Etudiant> etudiants) {
        return new PageImpl<>(fetchBagRelationships(etudiants.getContent()), etudiants.getPageable(), etudiants.getTotalElements());
    }

    @Override
    public List<Etudiant> fetchBagRelationships(List<Etudiant> etudiants) {
        return Optional.of(etudiants).map(this::fetchClubs).orElse(Collections.emptyList());
    }

    Etudiant fetchClubs(Etudiant result) {
        return entityManager
            .createQuery("select etudiant from Etudiant etudiant left join fetch etudiant.clubs where etudiant.id = :id", Etudiant.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Etudiant> fetchClubs(List<Etudiant> etudiants) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, etudiants.size()).forEach(index -> order.put(etudiants.get(index).getId(), index));
        List<Etudiant> result = entityManager
            .createQuery(
                "select etudiant from Etudiant etudiant left join fetch etudiant.clubs where etudiant in :etudiants",
                Etudiant.class
            )
            .setParameter("etudiants", etudiants)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
