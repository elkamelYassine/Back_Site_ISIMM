package tn.isimm.manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.isimm.manager.domain.CoursTestSamples.*;
import static tn.isimm.manager.domain.MatiereTestSamples.*;

import org.junit.jupiter.api.Test;
import tn.isimm.manager.web.rest.TestUtil;

class CoursTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cours.class);
        Cours cours1 = getCoursSample1();
        Cours cours2 = new Cours();
        assertThat(cours1).isNotEqualTo(cours2);

        cours2.setId(cours1.getId());
        assertThat(cours1).isEqualTo(cours2);

        cours2 = getCoursSample2();
        assertThat(cours1).isNotEqualTo(cours2);
    }

    @Test
    void matiereTest() throws Exception {
        Cours cours = getCoursRandomSampleGenerator();
        Matiere matiereBack = getMatiereRandomSampleGenerator();

        cours.setMatiere(matiereBack);
        assertThat(cours.getMatiere()).isEqualTo(matiereBack);

        cours.matiere(null);
        assertThat(cours.getMatiere()).isNull();
    }
}
