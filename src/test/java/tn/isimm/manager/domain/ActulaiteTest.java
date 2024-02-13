package tn.isimm.manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.isimm.manager.domain.ActulaiteTestSamples.*;

import org.junit.jupiter.api.Test;
import tn.isimm.manager.web.rest.TestUtil;

class ActulaiteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Actulaite.class);
        Actulaite actulaite1 = getActulaiteSample1();
        Actulaite actulaite2 = new Actulaite();
        assertThat(actulaite1).isNotEqualTo(actulaite2);

        actulaite2.setId(actulaite1.getId());
        assertThat(actulaite1).isEqualTo(actulaite2);

        actulaite2 = getActulaiteSample2();
        assertThat(actulaite1).isNotEqualTo(actulaite2);
    }
}
