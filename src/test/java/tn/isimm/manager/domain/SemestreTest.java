package tn.isimm.manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.isimm.manager.domain.MatiereTestSamples.*;
import static tn.isimm.manager.domain.NiveauTestSamples.*;
import static tn.isimm.manager.domain.SemestreTestSamples.*;

import org.junit.jupiter.api.Test;
import tn.isimm.manager.web.rest.TestUtil;

class SemestreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Semestre.class);
        Semestre semestre1 = getSemestreSample1();
        Semestre semestre2 = new Semestre();
        assertThat(semestre1).isNotEqualTo(semestre2);

        semestre2.setId(semestre1.getId());
        assertThat(semestre1).isEqualTo(semestre2);

        semestre2 = getSemestreSample2();
        assertThat(semestre1).isNotEqualTo(semestre2);
    }

    @Test
    void niveauTest() throws Exception {
        Semestre semestre = getSemestreRandomSampleGenerator();
        Niveau niveauBack = getNiveauRandomSampleGenerator();

        semestre.setNiveau(niveauBack);
        assertThat(semestre.getNiveau()).isEqualTo(niveauBack);
        assertThat(niveauBack.getSemestre()).isEqualTo(semestre);

        semestre.niveau(null);
        assertThat(semestre.getNiveau()).isNull();
        assertThat(niveauBack.getSemestre()).isNull();
    }

    @Test
    void matiereTest() throws Exception {
        Semestre semestre = getSemestreRandomSampleGenerator();
        Matiere matiereBack = getMatiereRandomSampleGenerator();

        semestre.setMatiere(matiereBack);
        assertThat(semestre.getMatiere()).isEqualTo(matiereBack);
        assertThat(matiereBack.getSemestre()).isEqualTo(semestre);

        semestre.matiere(null);
        assertThat(semestre.getMatiere()).isNull();
        assertThat(matiereBack.getSemestre()).isNull();
    }
}
