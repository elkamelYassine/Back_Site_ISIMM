package tn.isimm.manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.isimm.manager.domain.EtudiantTestSamples.*;
import static tn.isimm.manager.domain.NiveauTestSamples.*;
import static tn.isimm.manager.domain.SeanceTestSamples.*;
import static tn.isimm.manager.domain.SemestreTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import tn.isimm.manager.web.rest.TestUtil;

class NiveauTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Niveau.class);
        Niveau niveau1 = getNiveauSample1();
        Niveau niveau2 = new Niveau();
        assertThat(niveau1).isNotEqualTo(niveau2);

        niveau2.setId(niveau1.getId());
        assertThat(niveau1).isEqualTo(niveau2);

        niveau2 = getNiveauSample2();
        assertThat(niveau1).isNotEqualTo(niveau2);
    }

    @Test
    void semestreTest() throws Exception {
        Niveau niveau = getNiveauRandomSampleGenerator();
        Semestre semestreBack = getSemestreRandomSampleGenerator();

        niveau.setSemestre(semestreBack);
        assertThat(niveau.getSemestre()).isEqualTo(semestreBack);

        niveau.semestre(null);
        assertThat(niveau.getSemestre()).isNull();
    }

    @Test
    void seanceTest() throws Exception {
        Niveau niveau = getNiveauRandomSampleGenerator();
        Seance seanceBack = getSeanceRandomSampleGenerator();

        niveau.addSeance(seanceBack);
        assertThat(niveau.getSeances()).containsOnly(seanceBack);
        assertThat(seanceBack.getNiveau()).isEqualTo(niveau);

        niveau.removeSeance(seanceBack);
        assertThat(niveau.getSeances()).doesNotContain(seanceBack);
        assertThat(seanceBack.getNiveau()).isNull();

        niveau.seances(new HashSet<>(Set.of(seanceBack)));
        assertThat(niveau.getSeances()).containsOnly(seanceBack);
        assertThat(seanceBack.getNiveau()).isEqualTo(niveau);

        niveau.setSeances(new HashSet<>());
        assertThat(niveau.getSeances()).doesNotContain(seanceBack);
        assertThat(seanceBack.getNiveau()).isNull();
    }

    @Test
    void etudiantTest() throws Exception {
        Niveau niveau = getNiveauRandomSampleGenerator();
        Etudiant etudiantBack = getEtudiantRandomSampleGenerator();

        niveau.addEtudiant(etudiantBack);
        assertThat(niveau.getEtudiants()).containsOnly(etudiantBack);
        assertThat(etudiantBack.getNiveau()).isEqualTo(niveau);

        niveau.removeEtudiant(etudiantBack);
        assertThat(niveau.getEtudiants()).doesNotContain(etudiantBack);
        assertThat(etudiantBack.getNiveau()).isNull();

        niveau.etudiants(new HashSet<>(Set.of(etudiantBack)));
        assertThat(niveau.getEtudiants()).containsOnly(etudiantBack);
        assertThat(etudiantBack.getNiveau()).isEqualTo(niveau);

        niveau.setEtudiants(new HashSet<>());
        assertThat(niveau.getEtudiants()).doesNotContain(etudiantBack);
        assertThat(etudiantBack.getNiveau()).isNull();
    }
}
