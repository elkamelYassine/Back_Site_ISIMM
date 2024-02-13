package tn.isimm.manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.isimm.manager.domain.MatiereTestSamples.*;
import static tn.isimm.manager.domain.NiveauTestSamples.*;
import static tn.isimm.manager.domain.SeanceTestSamples.*;

import org.junit.jupiter.api.Test;
import tn.isimm.manager.web.rest.TestUtil;

class SeanceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Seance.class);
        Seance seance1 = getSeanceSample1();
        Seance seance2 = new Seance();
        assertThat(seance1).isNotEqualTo(seance2);

        seance2.setId(seance1.getId());
        assertThat(seance1).isEqualTo(seance2);

        seance2 = getSeanceSample2();
        assertThat(seance1).isNotEqualTo(seance2);
    }

    @Test
    void matiereTest() throws Exception {
        Seance seance = getSeanceRandomSampleGenerator();
        Matiere matiereBack = getMatiereRandomSampleGenerator();

        seance.setMatiere(matiereBack);
        assertThat(seance.getMatiere()).isEqualTo(matiereBack);

        seance.matiere(null);
        assertThat(seance.getMatiere()).isNull();
    }

    @Test
    void niveauTest() throws Exception {
        Seance seance = getSeanceRandomSampleGenerator();
        Niveau niveauBack = getNiveauRandomSampleGenerator();

        seance.setNiveau(niveauBack);
        assertThat(seance.getNiveau()).isEqualTo(niveauBack);

        seance.niveau(null);
        assertThat(seance.getNiveau()).isNull();
    }
}
