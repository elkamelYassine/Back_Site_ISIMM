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
import tn.isimm.manager.domain.Semestre;
import tn.isimm.manager.repository.SemestreRepository;

/**
 * Integration tests for the {@link SemestreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SemestreResourceIT {

    private static final String DEFAULT_ANNEE_SCOLAIRE = "AAAAAAAAAA";
    private static final String UPDATED_ANNEE_SCOLAIRE = "BBBBBBBBBB";

    private static final Integer DEFAULT_S = 1;
    private static final Integer UPDATED_S = 2;

    private static final String ENTITY_API_URL = "/api/semestres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SemestreRepository semestreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSemestreMockMvc;

    private Semestre semestre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Semestre createEntity(EntityManager em) {
        Semestre semestre = new Semestre().anneeScolaire(DEFAULT_ANNEE_SCOLAIRE).s(DEFAULT_S);
        return semestre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Semestre createUpdatedEntity(EntityManager em) {
        Semestre semestre = new Semestre().anneeScolaire(UPDATED_ANNEE_SCOLAIRE).s(UPDATED_S);
        return semestre;
    }

    @BeforeEach
    public void initTest() {
        semestre = createEntity(em);
    }

    @Test
    @Transactional
    void createSemestre() throws Exception {
        int databaseSizeBeforeCreate = semestreRepository.findAll().size();
        // Create the Semestre
        restSemestreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(semestre)))
            .andExpect(status().isCreated());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeCreate + 1);
        Semestre testSemestre = semestreList.get(semestreList.size() - 1);
        assertThat(testSemestre.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);
        assertThat(testSemestre.getS()).isEqualTo(DEFAULT_S);
    }

    @Test
    @Transactional
    void createSemestreWithExistingId() throws Exception {
        // Create the Semestre with an existing ID
        semestre.setId(1L);

        int databaseSizeBeforeCreate = semestreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSemestreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(semestre)))
            .andExpect(status().isBadRequest());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSemestres() throws Exception {
        // Initialize the database
        semestreRepository.saveAndFlush(semestre);

        // Get all the semestreList
        restSemestreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(semestre.getId().intValue())))
            .andExpect(jsonPath("$.[*].anneeScolaire").value(hasItem(DEFAULT_ANNEE_SCOLAIRE)))
            .andExpect(jsonPath("$.[*].s").value(hasItem(DEFAULT_S)));
    }

    @Test
    @Transactional
    void getSemestre() throws Exception {
        // Initialize the database
        semestreRepository.saveAndFlush(semestre);

        // Get the semestre
        restSemestreMockMvc
            .perform(get(ENTITY_API_URL_ID, semestre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(semestre.getId().intValue()))
            .andExpect(jsonPath("$.anneeScolaire").value(DEFAULT_ANNEE_SCOLAIRE))
            .andExpect(jsonPath("$.s").value(DEFAULT_S));
    }

    @Test
    @Transactional
    void getNonExistingSemestre() throws Exception {
        // Get the semestre
        restSemestreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSemestre() throws Exception {
        // Initialize the database
        semestreRepository.saveAndFlush(semestre);

        int databaseSizeBeforeUpdate = semestreRepository.findAll().size();

        // Update the semestre
        Semestre updatedSemestre = semestreRepository.findById(semestre.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSemestre are not directly saved in db
        em.detach(updatedSemestre);
        updatedSemestre.anneeScolaire(UPDATED_ANNEE_SCOLAIRE).s(UPDATED_S);

        restSemestreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSemestre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSemestre))
            )
            .andExpect(status().isOk());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeUpdate);
        Semestre testSemestre = semestreList.get(semestreList.size() - 1);
        assertThat(testSemestre.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);
        assertThat(testSemestre.getS()).isEqualTo(UPDATED_S);
    }

    @Test
    @Transactional
    void putNonExistingSemestre() throws Exception {
        int databaseSizeBeforeUpdate = semestreRepository.findAll().size();
        semestre.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSemestreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, semestre.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(semestre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSemestre() throws Exception {
        int databaseSizeBeforeUpdate = semestreRepository.findAll().size();
        semestre.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemestreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(semestre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSemestre() throws Exception {
        int databaseSizeBeforeUpdate = semestreRepository.findAll().size();
        semestre.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemestreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(semestre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSemestreWithPatch() throws Exception {
        // Initialize the database
        semestreRepository.saveAndFlush(semestre);

        int databaseSizeBeforeUpdate = semestreRepository.findAll().size();

        // Update the semestre using partial update
        Semestre partialUpdatedSemestre = new Semestre();
        partialUpdatedSemestre.setId(semestre.getId());

        partialUpdatedSemestre.s(UPDATED_S);

        restSemestreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSemestre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSemestre))
            )
            .andExpect(status().isOk());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeUpdate);
        Semestre testSemestre = semestreList.get(semestreList.size() - 1);
        assertThat(testSemestre.getAnneeScolaire()).isEqualTo(DEFAULT_ANNEE_SCOLAIRE);
        assertThat(testSemestre.getS()).isEqualTo(UPDATED_S);
    }

    @Test
    @Transactional
    void fullUpdateSemestreWithPatch() throws Exception {
        // Initialize the database
        semestreRepository.saveAndFlush(semestre);

        int databaseSizeBeforeUpdate = semestreRepository.findAll().size();

        // Update the semestre using partial update
        Semestre partialUpdatedSemestre = new Semestre();
        partialUpdatedSemestre.setId(semestre.getId());

        partialUpdatedSemestre.anneeScolaire(UPDATED_ANNEE_SCOLAIRE).s(UPDATED_S);

        restSemestreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSemestre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSemestre))
            )
            .andExpect(status().isOk());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeUpdate);
        Semestre testSemestre = semestreList.get(semestreList.size() - 1);
        assertThat(testSemestre.getAnneeScolaire()).isEqualTo(UPDATED_ANNEE_SCOLAIRE);
        assertThat(testSemestre.getS()).isEqualTo(UPDATED_S);
    }

    @Test
    @Transactional
    void patchNonExistingSemestre() throws Exception {
        int databaseSizeBeforeUpdate = semestreRepository.findAll().size();
        semestre.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSemestreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, semestre.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(semestre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSemestre() throws Exception {
        int databaseSizeBeforeUpdate = semestreRepository.findAll().size();
        semestre.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemestreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(semestre))
            )
            .andExpect(status().isBadRequest());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSemestre() throws Exception {
        int databaseSizeBeforeUpdate = semestreRepository.findAll().size();
        semestre.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemestreMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(semestre)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Semestre in the database
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSemestre() throws Exception {
        // Initialize the database
        semestreRepository.saveAndFlush(semestre);

        int databaseSizeBeforeDelete = semestreRepository.findAll().size();

        // Delete the semestre
        restSemestreMockMvc
            .perform(delete(ENTITY_API_URL_ID, semestre.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Semestre> semestreList = semestreRepository.findAll();
        assertThat(semestreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
