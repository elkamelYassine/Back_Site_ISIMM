package tn.isimm.manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.isimm.manager.domain.ClubTestSamples.*;
import static tn.isimm.manager.domain.EtudiantTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import tn.isimm.manager.web.rest.TestUtil;

class ClubTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Club.class);
        Club club1 = getClubSample1();
        Club club2 = new Club();
        assertThat(club1).isNotEqualTo(club2);

        club2.setId(club1.getId());
        assertThat(club1).isEqualTo(club2);

        club2 = getClubSample2();
        assertThat(club1).isNotEqualTo(club2);
    }

    @Test
    void etudiantTest() throws Exception {
        Club club = getClubRandomSampleGenerator();
        Etudiant etudiantBack = getEtudiantRandomSampleGenerator();

        club.addEtudiant(etudiantBack);
        assertThat(club.getEtudiants()).containsOnly(etudiantBack);
        assertThat(etudiantBack.getClubs()).containsOnly(club);

        club.removeEtudiant(etudiantBack);
        assertThat(club.getEtudiants()).doesNotContain(etudiantBack);
        assertThat(etudiantBack.getClubs()).doesNotContain(club);

        club.etudiants(new HashSet<>(Set.of(etudiantBack)));
        assertThat(club.getEtudiants()).containsOnly(etudiantBack);
        assertThat(etudiantBack.getClubs()).containsOnly(club);

        club.setEtudiants(new HashSet<>());
        assertThat(club.getEtudiants()).doesNotContain(etudiantBack);
        assertThat(etudiantBack.getClubs()).doesNotContain(club);
    }
}
