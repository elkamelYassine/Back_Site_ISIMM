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
import tn.isimm.manager.domain.Seance;
import tn.isimm.manager.domain.enumeration.Jours;
import tn.isimm.manager.domain.enumeration.Salle;
import tn.isimm.manager.repository.SeanceRepository;

/**
 * Integration tests for the {@link SeanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SeanceResourceIT {

    private static final Jours DEFAULT_JOUR = Jours.Lundi;
    private static final Jours UPDATED_JOUR = Jours.Mardi;

    private static final Integer DEFAULT_NUM_SEANCE = 1;
    private static final Integer UPDATED_NUM_SEANCE = 2;

    private static final Salle DEFAULT_SALLE = Salle.A01;
    private static final Salle UPDATED_SALLE = Salle.A02;

    private static final String ENTITY_API_URL = "/api/seances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SeanceRepository seanceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSeanceMockMvc;

    private Seance seance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seance createEntity(EntityManager em) {
        Seance seance = new Seance().jour(DEFAULT_JOUR).numSeance(DEFAULT_NUM_SEANCE).salle(DEFAULT_SALLE);
        return seance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seance createUpdatedEntity(EntityManager em) {
        Seance seance = new Seance().jour(UPDATED_JOUR).numSeance(UPDATED_NUM_SEANCE).salle(UPDATED_SALLE);
        return seance;
    }

    @BeforeEach
    public void initTest() {
        seance = createEntity(em);
    }

    @Test
    @Transactional
    void createSeance() throws Exception {
        int databaseSizeBeforeCreate = seanceRepository.findAll().size();
        // Create the Seance
        restSeanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seance)))
            .andExpect(status().isCreated());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeCreate + 1);
        Seance testSeance = seanceList.get(seanceList.size() - 1);
        assertThat(testSeance.getJour()).isEqualTo(DEFAULT_JOUR);
        assertThat(testSeance.getNumSeance()).isEqualTo(DEFAULT_NUM_SEANCE);
        assertThat(testSeance.getSalle()).isEqualTo(DEFAULT_SALLE);
    }

    @Test
    @Transactional
    void createSeanceWithExistingId() throws Exception {
        // Create the Seance with an existing ID
        seance.setId(1L);

        int databaseSizeBeforeCreate = seanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seance)))
            .andExpect(status().isBadRequest());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSeances() throws Exception {
        // Initialize the database
        seanceRepository.saveAndFlush(seance);

        // Get all the seanceList
        restSeanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seance.getId().intValue())))
            .andExpect(jsonPath("$.[*].jour").value(hasItem(DEFAULT_JOUR.toString())))
            .andExpect(jsonPath("$.[*].numSeance").value(hasItem(DEFAULT_NUM_SEANCE)))
            .andExpect(jsonPath("$.[*].salle").value(hasItem(DEFAULT_SALLE.toString())));
    }

    @Test
    @Transactional
    void getSeance() throws Exception {
        // Initialize the database
        seanceRepository.saveAndFlush(seance);

        // Get the seance
        restSeanceMockMvc
            .perform(get(ENTITY_API_URL_ID, seance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(seance.getId().intValue()))
            .andExpect(jsonPath("$.jour").value(DEFAULT_JOUR.toString()))
            .andExpect(jsonPath("$.numSeance").value(DEFAULT_NUM_SEANCE))
            .andExpect(jsonPath("$.salle").value(DEFAULT_SALLE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSeance() throws Exception {
        // Get the seance
        restSeanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSeance() throws Exception {
        // Initialize the database
        seanceRepository.saveAndFlush(seance);

        int databaseSizeBeforeUpdate = seanceRepository.findAll().size();

        // Update the seance
        Seance updatedSeance = seanceRepository.findById(seance.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSeance are not directly saved in db
        em.detach(updatedSeance);
        updatedSeance.jour(UPDATED_JOUR).numSeance(UPDATED_NUM_SEANCE).salle(UPDATED_SALLE);

        restSeanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSeance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSeance))
            )
            .andExpect(status().isOk());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeUpdate);
        Seance testSeance = seanceList.get(seanceList.size() - 1);
        assertThat(testSeance.getJour()).isEqualTo(UPDATED_JOUR);
        assertThat(testSeance.getNumSeance()).isEqualTo(UPDATED_NUM_SEANCE);
        assertThat(testSeance.getSalle()).isEqualTo(UPDATED_SALLE);
    }

    @Test
    @Transactional
    void putNonExistingSeance() throws Exception {
        int databaseSizeBeforeUpdate = seanceRepository.findAll().size();
        seance.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeance() throws Exception {
        int databaseSizeBeforeUpdate = seanceRepository.findAll().size();
        seance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeance() throws Exception {
        int databaseSizeBeforeUpdate = seanceRepository.findAll().size();
        seance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeanceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSeanceWithPatch() throws Exception {
        // Initialize the database
        seanceRepository.saveAndFlush(seance);

        int databaseSizeBeforeUpdate = seanceRepository.findAll().size();

        // Update the seance using partial update
        Seance partialUpdatedSeance = new Seance();
        partialUpdatedSeance.setId(seance.getId());

        restSeanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeance))
            )
            .andExpect(status().isOk());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeUpdate);
        Seance testSeance = seanceList.get(seanceList.size() - 1);
        assertThat(testSeance.getJour()).isEqualTo(DEFAULT_JOUR);
        assertThat(testSeance.getNumSeance()).isEqualTo(DEFAULT_NUM_SEANCE);
        assertThat(testSeance.getSalle()).isEqualTo(DEFAULT_SALLE);
    }

    @Test
    @Transactional
    void fullUpdateSeanceWithPatch() throws Exception {
        // Initialize the database
        seanceRepository.saveAndFlush(seance);

        int databaseSizeBeforeUpdate = seanceRepository.findAll().size();

        // Update the seance using partial update
        Seance partialUpdatedSeance = new Seance();
        partialUpdatedSeance.setId(seance.getId());

        partialUpdatedSeance.jour(UPDATED_JOUR).numSeance(UPDATED_NUM_SEANCE).salle(UPDATED_SALLE);

        restSeanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeance))
            )
            .andExpect(status().isOk());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeUpdate);
        Seance testSeance = seanceList.get(seanceList.size() - 1);
        assertThat(testSeance.getJour()).isEqualTo(UPDATED_JOUR);
        assertThat(testSeance.getNumSeance()).isEqualTo(UPDATED_NUM_SEANCE);
        assertThat(testSeance.getSalle()).isEqualTo(UPDATED_SALLE);
    }

    @Test
    @Transactional
    void patchNonExistingSeance() throws Exception {
        int databaseSizeBeforeUpdate = seanceRepository.findAll().size();
        seance.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, seance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeance() throws Exception {
        int databaseSizeBeforeUpdate = seanceRepository.findAll().size();
        seance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeance() throws Exception {
        int databaseSizeBeforeUpdate = seanceRepository.findAll().size();
        seance.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeanceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(seance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seance in the database
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSeance() throws Exception {
        // Initialize the database
        seanceRepository.saveAndFlush(seance);

        int databaseSizeBeforeDelete = seanceRepository.findAll().size();

        // Delete the seance
        restSeanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, seance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Seance> seanceList = seanceRepository.findAll();
        assertThat(seanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
