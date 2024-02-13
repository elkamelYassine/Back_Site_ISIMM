package tn.isimm.manager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tn.isimm.manager.IntegrationTest;
import tn.isimm.manager.domain.Note;
import tn.isimm.manager.repository.NoteRepository;

/**
 * Integration tests for the {@link NoteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NoteResourceIT {

    private static final Integer DEFAULT_NOTE = 1;
    private static final Integer UPDATED_NOTE = 2;

    private static final String ENTITY_API_URL = "/api/notes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNoteMockMvc;

    private Note note;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Note createEntity(EntityManager em) {
        Note note = new Note().note(DEFAULT_NOTE);
        return note;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Note createUpdatedEntity(EntityManager em) {
        Note note = new Note().note(UPDATED_NOTE);
        return note;
    }

    @BeforeEach
    public void initTest() {
        note = createEntity(em);
    }

    @Test
    @Transactional
    void createNote() throws Exception {
        int databaseSizeBeforeCreate = noteRepository.findAll().size();
        // Create the Note
        restNoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(note)))
            .andExpect(status().isCreated());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeCreate + 1);
        Note testNote = noteList.get(noteList.size() - 1);
        assertThat(testNote.getNote()).isEqualTo(DEFAULT_NOTE);
    }

    @Test
    @Transactional
    void createNoteWithExistingId() throws Exception {
        // Create the Note with an existing ID
        note.setId(1L);

        int databaseSizeBeforeCreate = noteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNoteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(note)))
            .andExpect(status().isBadRequest());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNotes() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get all the noteList
        restNoteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(note.getId().intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)));
    }

    @Test
    @Transactional
    void getNote() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        // Get the note
        restNoteMockMvc
            .perform(get(ENTITY_API_URL_ID, note.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(note.getId().intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE));
    }

    @Test
    @Transactional
    void getNonExistingNote() throws Exception {
        // Get the note
        restNoteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNote() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        int databaseSizeBeforeUpdate = noteRepository.findAll().size();

        // Update the note
        Note updatedNote = noteRepository.findById(note.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNote are not directly saved in db
        em.detach(updatedNote);
        updatedNote.note(UPDATED_NOTE);

        restNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNote.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNote))
            )
            .andExpect(status().isOk());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
        Note testNote = noteList.get(noteList.size() - 1);
        assertThat(testNote.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void putNonExistingNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().size();
        note.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, note.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(note))
            )
            .andExpect(status().isBadRequest());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().size();
        note.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNoteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(note))
            )
            .andExpect(status().isBadRequest());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().size();
        note.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNoteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(note)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNoteWithPatch() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        int databaseSizeBeforeUpdate = noteRepository.findAll().size();

        // Update the note using partial update
        Note partialUpdatedNote = new Note();
        partialUpdatedNote.setId(note.getId());

        partialUpdatedNote.note(UPDATED_NOTE);

        restNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNote))
            )
            .andExpect(status().isOk());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
        Note testNote = noteList.get(noteList.size() - 1);
        assertThat(testNote.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void fullUpdateNoteWithPatch() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        int databaseSizeBeforeUpdate = noteRepository.findAll().size();

        // Update the note using partial update
        Note partialUpdatedNote = new Note();
        partialUpdatedNote.setId(note.getId());

        partialUpdatedNote.note(UPDATED_NOTE);

        restNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNote.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNote))
            )
            .andExpect(status().isOk());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
        Note testNote = noteList.get(noteList.size() - 1);
        assertThat(testNote.getNote()).isEqualTo(UPDATED_NOTE);
    }

    @Test
    @Transactional
    void patchNonExistingNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().size();
        note.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, note.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(note))
            )
            .andExpect(status().isBadRequest());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().size();
        note.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNoteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(note))
            )
            .andExpect(status().isBadRequest());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNote() throws Exception {
        int databaseSizeBeforeUpdate = noteRepository.findAll().size();
        note.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNoteMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(note)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Note in the database
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNote() throws Exception {
        // Initialize the database
        noteRepository.saveAndFlush(note);

        int databaseSizeBeforeDelete = noteRepository.findAll().size();

        // Delete the note
        restNoteMockMvc
            .perform(delete(ENTITY_API_URL_ID, note.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Note> noteList = noteRepository.findAll();
        assertThat(noteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
