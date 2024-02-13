package tn.isimm.manager.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static tn.isimm.manager.domain.MatiereTestSamples.*;
import static tn.isimm.manager.domain.NoteTestSamples.*;

import org.junit.jupiter.api.Test;
import tn.isimm.manager.web.rest.TestUtil;

class NoteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Note.class);
        Note note1 = getNoteSample1();
        Note note2 = new Note();
        assertThat(note1).isNotEqualTo(note2);

        note2.setId(note1.getId());
        assertThat(note1).isEqualTo(note2);

        note2 = getNoteSample2();
        assertThat(note1).isNotEqualTo(note2);
    }

    @Test
    void matiereTest() throws Exception {
        Note note = getNoteRandomSampleGenerator();
        Matiere matiereBack = getMatiereRandomSampleGenerator();

        note.setMatiere(matiereBack);
        assertThat(note.getMatiere()).isEqualTo(matiereBack);
        assertThat(matiereBack.getNote()).isEqualTo(note);

        note.matiere(null);
        assertThat(note.getMatiere()).isNull();
        assertThat(matiereBack.getNote()).isNull();
    }
}
