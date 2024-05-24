package tn.isimm.manager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Base64;
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
import tn.isimm.manager.domain.Actulaite;
import tn.isimm.manager.repository.ActulaiteRepository;

/**
 * Integration tests for the {@link ActulaiteResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActulaiteResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/actulaites";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ActulaiteRepository actulaiteRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActulaiteMockMvc;

    private Actulaite actulaite;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Actulaite createEntity(EntityManager em) {
        Actulaite actulaite = new Actulaite()
            .date(DEFAULT_DATE)
            .data(DEFAULT_DATA)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION);
        return actulaite;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Actulaite createUpdatedEntity(EntityManager em) {
        Actulaite actulaite = new Actulaite()
            .date(UPDATED_DATE)
            .data(UPDATED_DATA)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION);
        return actulaite;
    }

    @BeforeEach
    public void initTest() {
        actulaite = createEntity(em);
    }

    @Test
    @Transactional
    void createActulaite() throws Exception {
        int databaseSizeBeforeCreate = actulaiteRepository.findAll().size();
        // Create the Actulaite
        restActulaiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(actulaite)))
            .andExpect(status().isCreated());

        // Validate the Actulaite in the database
        List<Actulaite> actulaiteList = actulaiteRepository.findAll();
        assertThat(actulaiteList).hasSize(databaseSizeBeforeCreate + 1);
        Actulaite testActulaite = actulaiteList.get(actulaiteList.size() - 1);
        assertThat(testActulaite.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testActulaite.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testActulaite.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testActulaite.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testActulaite.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testActulaite.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createActulaiteWithExistingId() throws Exception {
        // Create the Actulaite with an existing ID
        actulaite.setId(1L);

        int databaseSizeBeforeCreate = actulaiteRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActulaiteMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(actulaite)))
            .andExpect(status().isBadRequest());

        // Validate the Actulaite in the database
        List<Actulaite> actulaiteList = actulaiteRepository.findAll();
        assertThat(actulaiteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllActulaites() throws Exception {
        // Initialize the database
        actulaiteRepository.saveAndFlush(actulaite);

        // Get all the actulaiteList
        restActulaiteMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(actulaite.getId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getActulaite() throws Exception {
        // Initialize the database
        actulaiteRepository.saveAndFlush(actulaite);

        // Get the actulaite
        restActulaiteMockMvc
            .perform(get(ENTITY_API_URL_ID, actulaite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(actulaite.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64.getEncoder().encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingActulaite() throws Exception {
        // Get the actulaite
        restActulaiteMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingActulaite() throws Exception {
        // Initialize the database
        actulaiteRepository.saveAndFlush(actulaite);

        int databaseSizeBeforeUpdate = actulaiteRepository.findAll().size();

        // Update the actulaite
        Actulaite updatedActulaite = actulaiteRepository.findById(actulaite.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedActulaite are not directly saved in db
        em.detach(updatedActulaite);
        updatedActulaite
            .date(UPDATED_DATE)
            .data(UPDATED_DATA)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION);

        restActulaiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedActulaite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedActulaite))
            )
            .andExpect(status().isOk());

        // Validate the Actulaite in the database
        List<Actulaite> actulaiteList = actulaiteRepository.findAll();
        assertThat(actulaiteList).hasSize(databaseSizeBeforeUpdate);
        Actulaite testActulaite = actulaiteList.get(actulaiteList.size() - 1);
        assertThat(testActulaite.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testActulaite.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testActulaite.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testActulaite.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testActulaite.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testActulaite.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingActulaite() throws Exception {
        int databaseSizeBeforeUpdate = actulaiteRepository.findAll().size();
        actulaite.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActulaiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, actulaite.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actulaite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Actulaite in the database
        List<Actulaite> actulaiteList = actulaiteRepository.findAll();
        assertThat(actulaiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActulaite() throws Exception {
        int databaseSizeBeforeUpdate = actulaiteRepository.findAll().size();
        actulaite.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActulaiteMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actulaite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Actulaite in the database
        List<Actulaite> actulaiteList = actulaiteRepository.findAll();
        assertThat(actulaiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActulaite() throws Exception {
        int databaseSizeBeforeUpdate = actulaiteRepository.findAll().size();
        actulaite.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActulaiteMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(actulaite)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Actulaite in the database
        List<Actulaite> actulaiteList = actulaiteRepository.findAll();
        assertThat(actulaiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActulaiteWithPatch() throws Exception {
        // Initialize the database
        actulaiteRepository.saveAndFlush(actulaite);

        int databaseSizeBeforeUpdate = actulaiteRepository.findAll().size();

        // Update the actulaite using partial update
        Actulaite partialUpdatedActulaite = new Actulaite();
        partialUpdatedActulaite.setId(actulaite.getId());

        partialUpdatedActulaite.date(UPDATED_DATE);

        restActulaiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActulaite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActulaite))
            )
            .andExpect(status().isOk());

        // Validate the Actulaite in the database
        List<Actulaite> actulaiteList = actulaiteRepository.findAll();
        assertThat(actulaiteList).hasSize(databaseSizeBeforeUpdate);
        Actulaite testActulaite = actulaiteList.get(actulaiteList.size() - 1);
        assertThat(testActulaite.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testActulaite.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testActulaite.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testActulaite.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testActulaite.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testActulaite.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateActulaiteWithPatch() throws Exception {
        // Initialize the database
        actulaiteRepository.saveAndFlush(actulaite);

        int databaseSizeBeforeUpdate = actulaiteRepository.findAll().size();

        // Update the actulaite using partial update
        Actulaite partialUpdatedActulaite = new Actulaite();
        partialUpdatedActulaite.setId(actulaite.getId());

        partialUpdatedActulaite
            .date(UPDATED_DATE)
            .data(UPDATED_DATA)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION);

        restActulaiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActulaite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActulaite))
            )
            .andExpect(status().isOk());

        // Validate the Actulaite in the database
        List<Actulaite> actulaiteList = actulaiteRepository.findAll();
        assertThat(actulaiteList).hasSize(databaseSizeBeforeUpdate);
        Actulaite testActulaite = actulaiteList.get(actulaiteList.size() - 1);
        assertThat(testActulaite.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testActulaite.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testActulaite.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testActulaite.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testActulaite.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testActulaite.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingActulaite() throws Exception {
        int databaseSizeBeforeUpdate = actulaiteRepository.findAll().size();
        actulaite.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActulaiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, actulaite.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(actulaite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Actulaite in the database
        List<Actulaite> actulaiteList = actulaiteRepository.findAll();
        assertThat(actulaiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActulaite() throws Exception {
        int databaseSizeBeforeUpdate = actulaiteRepository.findAll().size();
        actulaite.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActulaiteMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(actulaite))
            )
            .andExpect(status().isBadRequest());

        // Validate the Actulaite in the database
        List<Actulaite> actulaiteList = actulaiteRepository.findAll();
        assertThat(actulaiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActulaite() throws Exception {
        int databaseSizeBeforeUpdate = actulaiteRepository.findAll().size();
        actulaite.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActulaiteMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(actulaite))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Actulaite in the database
        List<Actulaite> actulaiteList = actulaiteRepository.findAll();
        assertThat(actulaiteList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActulaite() throws Exception {
        // Initialize the database
        actulaiteRepository.saveAndFlush(actulaite);

        int databaseSizeBeforeDelete = actulaiteRepository.findAll().size();

        // Delete the actulaite
        restActulaiteMockMvc
            .perform(delete(ENTITY_API_URL_ID, actulaite.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Actulaite> actulaiteList = actulaiteRepository.findAll();
        assertThat(actulaiteList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
