package tn.isimm.manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.isimm.manager.domain.ClubTestSamples.*;
import static tn.isimm.manager.domain.EtudiantTestSamples.*;
import static tn.isimm.manager.domain.FichierAdminTestSamples.*;
import static tn.isimm.manager.domain.NiveauTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import tn.isimm.manager.web.rest.TestUtil;

class EtudiantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Etudiant.class);
        Etudiant etudiant1 = getEtudiantSample1();
        Etudiant etudiant2 = new Etudiant();
        assertThat(etudiant1).isNotEqualTo(etudiant2);

        etudiant2.setId(etudiant1.getId());
        assertThat(etudiant1).isEqualTo(etudiant2);

        etudiant2 = getEtudiantSample2();
        assertThat(etudiant1).isNotEqualTo(etudiant2);
    }

    @Test
    void fichierAdminTest() throws Exception {
        Etudiant etudiant = getEtudiantRandomSampleGenerator();
        FichierAdmin fichierAdminBack = getFichierAdminRandomSampleGenerator();

        etudiant.addFichierAdmin(fichierAdminBack);
        assertThat(etudiant.getFichierAdmins()).containsOnly(fichierAdminBack);
        assertThat(fichierAdminBack.getEtudiant()).isEqualTo(etudiant);

        etudiant.removeFichierAdmin(fichierAdminBack);
        assertThat(etudiant.getFichierAdmins()).doesNotContain(fichierAdminBack);
        assertThat(fichierAdminBack.getEtudiant()).isNull();

        etudiant.fichierAdmins(new HashSet<>(Set.of(fichierAdminBack)));
        assertThat(etudiant.getFichierAdmins()).containsOnly(fichierAdminBack);
        assertThat(fichierAdminBack.getEtudiant()).isEqualTo(etudiant);

        etudiant.setFichierAdmins(new HashSet<>());
        assertThat(etudiant.getFichierAdmins()).doesNotContain(fichierAdminBack);
        assertThat(fichierAdminBack.getEtudiant()).isNull();
    }

    @Test
    void niveauTest() throws Exception {
        Etudiant etudiant = getEtudiantRandomSampleGenerator();
        Niveau niveauBack = getNiveauRandomSampleGenerator();

        etudiant.setNiveau(niveauBack);
        assertThat(etudiant.getNiveau()).isEqualTo(niveauBack);

        etudiant.niveau(null);
        assertThat(etudiant.getNiveau()).isNull();
    }

    @Test
    void clubTest() throws Exception {
        Etudiant etudiant = getEtudiantRandomSampleGenerator();
        Club clubBack = getClubRandomSampleGenerator();

        etudiant.addClub(clubBack);
        assertThat(etudiant.getClubs()).containsOnly(clubBack);

        etudiant.removeClub(clubBack);
        assertThat(etudiant.getClubs()).doesNotContain(clubBack);

        etudiant.clubs(new HashSet<>(Set.of(clubBack)));
        assertThat(etudiant.getClubs()).containsOnly(clubBack);

        etudiant.setClubs(new HashSet<>());
        assertThat(etudiant.getClubs()).doesNotContain(clubBack);
    }
}
