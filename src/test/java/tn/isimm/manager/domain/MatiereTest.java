package tn.isimm.manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.isimm.manager.domain.CoursTestSamples.*;
import static tn.isimm.manager.domain.MatiereTestSamples.*;
import static tn.isimm.manager.domain.NoteTestSamples.*;
import static tn.isimm.manager.domain.ProfesseurTestSamples.*;
import static tn.isimm.manager.domain.SeanceTestSamples.*;
import static tn.isimm.manager.domain.SemestreTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import tn.isimm.manager.web.rest.TestUtil;

class MatiereTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Matiere.class);
        Matiere matiere1 = getMatiereSample1();
        Matiere matiere2 = new Matiere();
        assertThat(matiere1).isNotEqualTo(matiere2);

        matiere2.setId(matiere1.getId());
        assertThat(matiere1).isEqualTo(matiere2);

        matiere2 = getMatiereSample2();
        assertThat(matiere1).isNotEqualTo(matiere2);
    }

    @Test
    void semestreTest() throws Exception {
        Matiere matiere = getMatiereRandomSampleGenerator();
        Semestre semestreBack = getSemestreRandomSampleGenerator();

        matiere.setSemestre(semestreBack);
        assertThat(matiere.getSemestre()).isEqualTo(semestreBack);

        matiere.semestre(null);
        assertThat(matiere.getSemestre()).isNull();
    }

    @Test
    void noteTest() throws Exception {
        Matiere matiere = getMatiereRandomSampleGenerator();
        Note noteBack = getNoteRandomSampleGenerator();

        matiere.setNote(noteBack);
        assertThat(matiere.getNote()).isEqualTo(noteBack);

        matiere.note(null);
        assertThat(matiere.getNote()).isNull();
    }

    @Test
    void coursTest() throws Exception {
        Matiere matiere = getMatiereRandomSampleGenerator();
        Cours coursBack = getCoursRandomSampleGenerator();

        matiere.addCours(coursBack);
        assertThat(matiere.getCours()).containsOnly(coursBack);
        assertThat(coursBack.getMatiere()).isEqualTo(matiere);

        matiere.removeCours(coursBack);
        assertThat(matiere.getCours()).doesNotContain(coursBack);
        assertThat(coursBack.getMatiere()).isNull();

        matiere.cours(new HashSet<>(Set.of(coursBack)));
        assertThat(matiere.getCours()).containsOnly(coursBack);
        assertThat(coursBack.getMatiere()).isEqualTo(matiere);

        matiere.setCours(new HashSet<>());
        assertThat(matiere.getCours()).doesNotContain(coursBack);
        assertThat(coursBack.getMatiere()).isNull();
    }

    @Test
    void seanceTest() throws Exception {
        Matiere matiere = getMatiereRandomSampleGenerator();
        Seance seanceBack = getSeanceRandomSampleGenerator();

        matiere.setSeance(seanceBack);
        assertThat(matiere.getSeance()).isEqualTo(seanceBack);
        assertThat(seanceBack.getMatiere()).isEqualTo(matiere);

        matiere.seance(null);
        assertThat(matiere.getSeance()).isNull();
        assertThat(seanceBack.getMatiere()).isNull();
    }

    @Test
    void professeurTest() throws Exception {
        Matiere matiere = getMatiereRandomSampleGenerator();
        Professeur professeurBack = getProfesseurRandomSampleGenerator();

        matiere.addProfesseur(professeurBack);
        assertThat(matiere.getProfesseurs()).containsOnly(professeurBack);
        assertThat(professeurBack.getMatieres()).containsOnly(matiere);

        matiere.removeProfesseur(professeurBack);
        assertThat(matiere.getProfesseurs()).doesNotContain(professeurBack);
        assertThat(professeurBack.getMatieres()).doesNotContain(matiere);

        matiere.professeurs(new HashSet<>(Set.of(professeurBack)));
        assertThat(matiere.getProfesseurs()).containsOnly(professeurBack);
        assertThat(professeurBack.getMatieres()).containsOnly(matiere);

        matiere.setProfesseurs(new HashSet<>());
        assertThat(matiere.getProfesseurs()).doesNotContain(professeurBack);
        assertThat(professeurBack.getMatieres()).doesNotContain(matiere);
    }
}
