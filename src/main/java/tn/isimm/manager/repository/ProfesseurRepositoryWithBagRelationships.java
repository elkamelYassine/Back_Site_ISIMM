package tn.isimm.manager.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import tn.isimm.manager.domain.Professeur;

public interface ProfesseurRepositoryWithBagRelationships {
    Optional<Professeur> fetchBagRelationships(Optional<Professeur> professeur);

    List<Professeur> fetchBagRelationships(List<Professeur> professeurs);

    Page<Professeur> fetchBagRelationships(Page<Professeur> professeurs);
}
