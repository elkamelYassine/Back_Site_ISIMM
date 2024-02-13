package tn.isimm.manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.isimm.manager.domain.EtudiantTestSamples.*;
import static tn.isimm.manager.domain.FichierAdminTestSamples.*;

import org.junit.jupiter.api.Test;
import tn.isimm.manager.web.rest.TestUtil;

class FichierAdminTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FichierAdmin.class);
        FichierAdmin fichierAdmin1 = getFichierAdminSample1();
        FichierAdmin fichierAdmin2 = new FichierAdmin();
        assertThat(fichierAdmin1).isNotEqualTo(fichierAdmin2);

        fichierAdmin2.setId(fichierAdmin1.getId());
        assertThat(fichierAdmin1).isEqualTo(fichierAdmin2);

        fichierAdmin2 = getFichierAdminSample2();
        assertThat(fichierAdmin1).isNotEqualTo(fichierAdmin2);
    }

    @Test
    void etudiantTest() throws Exception {
        FichierAdmin fichierAdmin = getFichierAdminRandomSampleGenerator();
        Etudiant etudiantBack = getEtudiantRandomSampleGenerator();

        fichierAdmin.setEtudiant(etudiantBack);
        assertThat(fichierAdmin.getEtudiant()).isEqualTo(etudiantBack);

        fichierAdmin.etudiant(null);
        assertThat(fichierAdmin.getEtudiant()).isNull();
    }
}
