package tn.isimm.manager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
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
import tn.isimm.manager.domain.FichierAdmin;
import tn.isimm.manager.domain.enumeration.TypeFichierAdmin;
import tn.isimm.manager.repository.FichierAdminRepository;

/**
 * Integration tests for the {@link FichierAdminResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FichierAdminResourceIT {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final TypeFichierAdmin DEFAULT_TYPE = TypeFichierAdmin.Presence;
    private static final TypeFichierAdmin UPDATED_TYPE = TypeFichierAdmin.Reusite;

    private static final byte[] DEFAULT_FILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FILE_CONTENT_TYPE = "image/png";

    private static final Boolean DEFAULT_DEMANDE_VALIDE = false;
    private static final Boolean UPDATED_DEMANDE_VALIDE = true;

    private static final String ENTITY_API_URL = "/api/fichier-admins";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FichierAdminRepository fichierAdminRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFichierAdminMockMvc;

    private FichierAdmin fichierAdmin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FichierAdmin createEntity(EntityManager em) {
        FichierAdmin fichierAdmin = new FichierAdmin()
            .titre(DEFAULT_TITRE)
            .type(DEFAULT_TYPE)
            .file(DEFAULT_FILE)
            .fileContentType(DEFAULT_FILE_CONTENT_TYPE)
            .demandeValide(DEFAULT_DEMANDE_VALIDE);
        return fichierAdmin;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FichierAdmin createUpdatedEntity(EntityManager em) {
        FichierAdmin fichierAdmin = new FichierAdmin()
            .titre(UPDATED_TITRE)
            .type(UPDATED_TYPE)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .demandeValide(UPDATED_DEMANDE_VALIDE);
        return fichierAdmin;
    }

    @BeforeEach
    public void initTest() {
        fichierAdmin = createEntity(em);
    }

    @Test
    @Transactional
    void createFichierAdmin() throws Exception {
        int databaseSizeBeforeCreate = fichierAdminRepository.findAll().size();
        // Create the FichierAdmin
        restFichierAdminMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fichierAdmin)))
            .andExpect(status().isCreated());

        // Validate the FichierAdmin in the database
        List<FichierAdmin> fichierAdminList = fichierAdminRepository.findAll();
        assertThat(fichierAdminList).hasSize(databaseSizeBeforeCreate + 1);
        FichierAdmin testFichierAdmin = fichierAdminList.get(fichierAdminList.size() - 1);
        assertThat(testFichierAdmin.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testFichierAdmin.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testFichierAdmin.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testFichierAdmin.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);
        assertThat(testFichierAdmin.getDemandeValide()).isEqualTo(DEFAULT_DEMANDE_VALIDE);
    }

    @Test
    @Transactional
    void createFichierAdminWithExistingId() throws Exception {
        // Create the FichierAdmin with an existing ID
        fichierAdmin.setId(1L);

        int databaseSizeBeforeCreate = fichierAdminRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFichierAdminMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fichierAdmin)))
            .andExpect(status().isBadRequest());

        // Validate the FichierAdmin in the database
        List<FichierAdmin> fichierAdminList = fichierAdminRepository.findAll();
        assertThat(fichierAdminList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFichierAdmins() throws Exception {
        // Initialize the database
        fichierAdminRepository.saveAndFlush(fichierAdmin);

        // Get all the fichierAdminList
        restFichierAdminMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(fichierAdmin.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fileContentType").value(hasItem(DEFAULT_FILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].file").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_FILE))))
            .andExpect(jsonPath("$.[*].demandeValide").value(hasItem(DEFAULT_DEMANDE_VALIDE.booleanValue())));
    }

    @Test
    @Transactional
    void getFichierAdmin() throws Exception {
        // Initialize the database
        fichierAdminRepository.saveAndFlush(fichierAdmin);

        // Get the fichierAdmin
        restFichierAdminMockMvc
            .perform(get(ENTITY_API_URL_ID, fichierAdmin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(fichierAdmin.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.fileContentType").value(DEFAULT_FILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.file").value(Base64.getEncoder().encodeToString(DEFAULT_FILE)))
            .andExpect(jsonPath("$.demandeValide").value(DEFAULT_DEMANDE_VALIDE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingFichierAdmin() throws Exception {
        // Get the fichierAdmin
        restFichierAdminMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFichierAdmin() throws Exception {
        // Initialize the database
        fichierAdminRepository.saveAndFlush(fichierAdmin);

        int databaseSizeBeforeUpdate = fichierAdminRepository.findAll().size();

        // Update the fichierAdmin
        FichierAdmin updatedFichierAdmin = fichierAdminRepository.findById(fichierAdmin.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFichierAdmin are not directly saved in db
        em.detach(updatedFichierAdmin);
        updatedFichierAdmin
            .titre(UPDATED_TITRE)
            .type(UPDATED_TYPE)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .demandeValide(UPDATED_DEMANDE_VALIDE);

        restFichierAdminMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFichierAdmin.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFichierAdmin))
            )
            .andExpect(status().isOk());

        // Validate the FichierAdmin in the database
        List<FichierAdmin> fichierAdminList = fichierAdminRepository.findAll();
        assertThat(fichierAdminList).hasSize(databaseSizeBeforeUpdate);
        FichierAdmin testFichierAdmin = fichierAdminList.get(fichierAdminList.size() - 1);
        assertThat(testFichierAdmin.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testFichierAdmin.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFichierAdmin.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testFichierAdmin.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
        assertThat(testFichierAdmin.getDemandeValide()).isEqualTo(UPDATED_DEMANDE_VALIDE);
    }

    @Test
    @Transactional
    void putNonExistingFichierAdmin() throws Exception {
        int databaseSizeBeforeUpdate = fichierAdminRepository.findAll().size();
        fichierAdmin.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFichierAdminMockMvc
            .perform(
                put(ENTITY_API_URL_ID, fichierAdmin.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fichierAdmin))
            )
            .andExpect(status().isBadRequest());

        // Validate the FichierAdmin in the database
        List<FichierAdmin> fichierAdminList = fichierAdminRepository.findAll();
        assertThat(fichierAdminList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFichierAdmin() throws Exception {
        int databaseSizeBeforeUpdate = fichierAdminRepository.findAll().size();
        fichierAdmin.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFichierAdminMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(fichierAdmin))
            )
            .andExpect(status().isBadRequest());

        // Validate the FichierAdmin in the database
        List<FichierAdmin> fichierAdminList = fichierAdminRepository.findAll();
        assertThat(fichierAdminList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFichierAdmin() throws Exception {
        int databaseSizeBeforeUpdate = fichierAdminRepository.findAll().size();
        fichierAdmin.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFichierAdminMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(fichierAdmin)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FichierAdmin in the database
        List<FichierAdmin> fichierAdminList = fichierAdminRepository.findAll();
        assertThat(fichierAdminList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFichierAdminWithPatch() throws Exception {
        // Initialize the database
        fichierAdminRepository.saveAndFlush(fichierAdmin);

        int databaseSizeBeforeUpdate = fichierAdminRepository.findAll().size();

        // Update the fichierAdmin using partial update
        FichierAdmin partialUpdatedFichierAdmin = new FichierAdmin();
        partialUpdatedFichierAdmin.setId(fichierAdmin.getId());

        partialUpdatedFichierAdmin.type(UPDATED_TYPE);

        restFichierAdminMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFichierAdmin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFichierAdmin))
            )
            .andExpect(status().isOk());

        // Validate the FichierAdmin in the database
        List<FichierAdmin> fichierAdminList = fichierAdminRepository.findAll();
        assertThat(fichierAdminList).hasSize(databaseSizeBeforeUpdate);
        FichierAdmin testFichierAdmin = fichierAdminList.get(fichierAdminList.size() - 1);
        assertThat(testFichierAdmin.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testFichierAdmin.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFichierAdmin.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testFichierAdmin.getFileContentType()).isEqualTo(DEFAULT_FILE_CONTENT_TYPE);
        assertThat(testFichierAdmin.getDemandeValide()).isEqualTo(DEFAULT_DEMANDE_VALIDE);
    }

    @Test
    @Transactional
    void fullUpdateFichierAdminWithPatch() throws Exception {
        // Initialize the database
        fichierAdminRepository.saveAndFlush(fichierAdmin);

        int databaseSizeBeforeUpdate = fichierAdminRepository.findAll().size();

        // Update the fichierAdmin using partial update
        FichierAdmin partialUpdatedFichierAdmin = new FichierAdmin();
        partialUpdatedFichierAdmin.setId(fichierAdmin.getId());

        partialUpdatedFichierAdmin
            .titre(UPDATED_TITRE)
            .type(UPDATED_TYPE)
            .file(UPDATED_FILE)
            .fileContentType(UPDATED_FILE_CONTENT_TYPE)
            .demandeValide(UPDATED_DEMANDE_VALIDE);

        restFichierAdminMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFichierAdmin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFichierAdmin))
            )
            .andExpect(status().isOk());

        // Validate the FichierAdmin in the database
        List<FichierAdmin> fichierAdminList = fichierAdminRepository.findAll();
        assertThat(fichierAdminList).hasSize(databaseSizeBeforeUpdate);
        FichierAdmin testFichierAdmin = fichierAdminList.get(fichierAdminList.size() - 1);
        assertThat(testFichierAdmin.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testFichierAdmin.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testFichierAdmin.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testFichierAdmin.getFileContentType()).isEqualTo(UPDATED_FILE_CONTENT_TYPE);
        assertThat(testFichierAdmin.getDemandeValide()).isEqualTo(UPDATED_DEMANDE_VALIDE);
    }

    @Test
    @Transactional
    void patchNonExistingFichierAdmin() throws Exception {
        int databaseSizeBeforeUpdate = fichierAdminRepository.findAll().size();
        fichierAdmin.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFichierAdminMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, fichierAdmin.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fichierAdmin))
            )
            .andExpect(status().isBadRequest());

        // Validate the FichierAdmin in the database
        List<FichierAdmin> fichierAdminList = fichierAdminRepository.findAll();
        assertThat(fichierAdminList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFichierAdmin() throws Exception {
        int databaseSizeBeforeUpdate = fichierAdminRepository.findAll().size();
        fichierAdmin.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFichierAdminMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(fichierAdmin))
            )
            .andExpect(status().isBadRequest());

        // Validate the FichierAdmin in the database
        List<FichierAdmin> fichierAdminList = fichierAdminRepository.findAll();
        assertThat(fichierAdminList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFichierAdmin() throws Exception {
        int databaseSizeBeforeUpdate = fichierAdminRepository.findAll().size();
        fichierAdmin.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFichierAdminMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(fichierAdmin))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FichierAdmin in the database
        List<FichierAdmin> fichierAdminList = fichierAdminRepository.findAll();
        assertThat(fichierAdminList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFichierAdmin() throws Exception {
        // Initialize the database
        fichierAdminRepository.saveAndFlush(fichierAdmin);

        int databaseSizeBeforeDelete = fichierAdminRepository.findAll().size();

        // Delete the fichierAdmin
        restFichierAdminMockMvc
            .perform(delete(ENTITY_API_URL_ID, fichierAdmin.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FichierAdmin> fichierAdminList = fichierAdminRepository.findAll();
        assertThat(fichierAdminList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
