package tn.isimm.manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.isimm.manager.domain.MatiereTestSamples.*;
import static tn.isimm.manager.domain.ProfesseurTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import tn.isimm.manager.web.rest.TestUtil;

class ProfesseurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Professeur.class);
        Professeur professeur1 = getProfesseurSample1();
        Professeur professeur2 = new Professeur();
        assertThat(professeur1).isNotEqualTo(professeur2);

        professeur2.setId(professeur1.getId());
        assertThat(professeur1).isEqualTo(professeur2);

        professeur2 = getProfesseurSample2();
        assertThat(professeur1).isNotEqualTo(professeur2);
    }

    @Test
    void matiereTest() throws Exception {
        Professeur professeur = getProfesseurRandomSampleGenerator();
        Matiere matiereBack = getMatiereRandomSampleGenerator();

        professeur.addMatiere(matiereBack);
        assertThat(professeur.getMatieres()).containsOnly(matiereBack);

        professeur.removeMatiere(matiereBack);
        assertThat(professeur.getMatieres()).doesNotContain(matiereBack);

        professeur.matieres(new HashSet<>(Set.of(matiereBack)));
        assertThat(professeur.getMatieres()).containsOnly(matiereBack);

        professeur.setMatieres(new HashSet<>());
        assertThat(professeur.getMatieres()).doesNotContain(matiereBack);
    }
}
