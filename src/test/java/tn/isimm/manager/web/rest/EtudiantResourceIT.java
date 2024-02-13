package tn.isimm.manager.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tn.isimm.manager.IntegrationTest;
import tn.isimm.manager.domain.Etudiant;
import tn.isimm.manager.repository.EtudiantRepository;

/**
 * Integration tests for the {@link EtudiantResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EtudiantResourceIT {

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final Long DEFAULT_NUM_ETUDIANT = 1L;
    private static final Long UPDATED_NUM_ETUDIANT = 2L;

    private static final String DEFAULT_NUM_TEL = "AAAAAAAAAA";
    private static final String UPDATED_NUM_TEL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DE_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DE_NAISSANCE = LocalDate.now(ZoneId.systemDefault());

    private static final byte[] DEFAULT_PHOTO_DE_PROFILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO_DE_PROFILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_DE_PROFILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_DE_PROFILE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/etudiants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Mock
    private EtudiantRepository etudiantRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEtudiantMockMvc;

    private Etudiant etudiant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etudiant createEntity(EntityManager em) {
        Etudiant etudiant = new Etudiant()
            .prenom(DEFAULT_PRENOM)
            .nom(DEFAULT_NOM)
            .email(DEFAULT_EMAIL)
            .numEtudiant(DEFAULT_NUM_ETUDIANT)
            .numTel(DEFAULT_NUM_TEL)
            .dateDeNaissance(DEFAULT_DATE_DE_NAISSANCE)
            .photoDeProfile(DEFAULT_PHOTO_DE_PROFILE)
            .photoDeProfileContentType(DEFAULT_PHOTO_DE_PROFILE_CONTENT_TYPE);
        return etudiant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Etudiant createUpdatedEntity(EntityManager em) {
        Etudiant etudiant = new Etudiant()
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .numEtudiant(UPDATED_NUM_ETUDIANT)
            .numTel(UPDATED_NUM_TEL)
            .dateDeNaissance(UPDATED_DATE_DE_NAISSANCE)
            .photoDeProfile(UPDATED_PHOTO_DE_PROFILE)
            .photoDeProfileContentType(UPDATED_PHOTO_DE_PROFILE_CONTENT_TYPE);
        return etudiant;
    }

    @BeforeEach
    public void initTest() {
        etudiant = createEntity(em);
    }

    @Test
    @Transactional
    void createEtudiant() throws Exception {
        int databaseSizeBeforeCreate = etudiantRepository.findAll().size();
        // Create the Etudiant
        restEtudiantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etudiant)))
            .andExpect(status().isCreated());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeCreate + 1);
        Etudiant testEtudiant = etudiantList.get(etudiantList.size() - 1);
        assertThat(testEtudiant.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testEtudiant.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testEtudiant.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testEtudiant.getNumEtudiant()).isEqualTo(DEFAULT_NUM_ETUDIANT);
        assertThat(testEtudiant.getNumTel()).isEqualTo(DEFAULT_NUM_TEL);
        assertThat(testEtudiant.getDateDeNaissance()).isEqualTo(DEFAULT_DATE_DE_NAISSANCE);
        assertThat(testEtudiant.getPhotoDeProfile()).isEqualTo(DEFAULT_PHOTO_DE_PROFILE);
        assertThat(testEtudiant.getPhotoDeProfileContentType()).isEqualTo(DEFAULT_PHOTO_DE_PROFILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createEtudiantWithExistingId() throws Exception {
        // Create the Etudiant with an existing ID
        etudiant.setId(1L);

        int databaseSizeBeforeCreate = etudiantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEtudiantMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etudiant)))
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEtudiants() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get all the etudiantList
        restEtudiantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(etudiant.getId().intValue())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].numEtudiant").value(hasItem(DEFAULT_NUM_ETUDIANT.intValue())))
            .andExpect(jsonPath("$.[*].numTel").value(hasItem(DEFAULT_NUM_TEL)))
            .andExpect(jsonPath("$.[*].dateDeNaissance").value(hasItem(DEFAULT_DATE_DE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].photoDeProfileContentType").value(hasItem(DEFAULT_PHOTO_DE_PROFILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photoDeProfile").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PHOTO_DE_PROFILE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEtudiantsWithEagerRelationshipsIsEnabled() throws Exception {
        when(etudiantRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEtudiantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(etudiantRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEtudiantsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(etudiantRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEtudiantMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(etudiantRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEtudiant() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        // Get the etudiant
        restEtudiantMockMvc
            .perform(get(ENTITY_API_URL_ID, etudiant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(etudiant.getId().intValue()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.numEtudiant").value(DEFAULT_NUM_ETUDIANT.intValue()))
            .andExpect(jsonPath("$.numTel").value(DEFAULT_NUM_TEL))
            .andExpect(jsonPath("$.dateDeNaissance").value(DEFAULT_DATE_DE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.photoDeProfileContentType").value(DEFAULT_PHOTO_DE_PROFILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.photoDeProfile").value(Base64.getEncoder().encodeToString(DEFAULT_PHOTO_DE_PROFILE)));
    }

    @Test
    @Transactional
    void getNonExistingEtudiant() throws Exception {
        // Get the etudiant
        restEtudiantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEtudiant() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();

        // Update the etudiant
        Etudiant updatedEtudiant = etudiantRepository.findById(etudiant.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEtudiant are not directly saved in db
        em.detach(updatedEtudiant);
        updatedEtudiant
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .numEtudiant(UPDATED_NUM_ETUDIANT)
            .numTel(UPDATED_NUM_TEL)
            .dateDeNaissance(UPDATED_DATE_DE_NAISSANCE)
            .photoDeProfile(UPDATED_PHOTO_DE_PROFILE)
            .photoDeProfileContentType(UPDATED_PHOTO_DE_PROFILE_CONTENT_TYPE);

        restEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEtudiant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeUpdate);
        Etudiant testEtudiant = etudiantList.get(etudiantList.size() - 1);
        assertThat(testEtudiant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEtudiant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEtudiant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEtudiant.getNumEtudiant()).isEqualTo(UPDATED_NUM_ETUDIANT);
        assertThat(testEtudiant.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
        assertThat(testEtudiant.getDateDeNaissance()).isEqualTo(UPDATED_DATE_DE_NAISSANCE);
        assertThat(testEtudiant.getPhotoDeProfile()).isEqualTo(UPDATED_PHOTO_DE_PROFILE);
        assertThat(testEtudiant.getPhotoDeProfileContentType()).isEqualTo(UPDATED_PHOTO_DE_PROFILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();
        etudiant.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, etudiant.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etudiant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();
        etudiant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(etudiant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();
        etudiant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(etudiant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEtudiantWithPatch() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();

        // Update the etudiant using partial update
        Etudiant partialUpdatedEtudiant = new Etudiant();
        partialUpdatedEtudiant.setId(etudiant.getId());

        partialUpdatedEtudiant
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .numEtudiant(UPDATED_NUM_ETUDIANT)
            .dateDeNaissance(UPDATED_DATE_DE_NAISSANCE);

        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtudiant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeUpdate);
        Etudiant testEtudiant = etudiantList.get(etudiantList.size() - 1);
        assertThat(testEtudiant.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testEtudiant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEtudiant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEtudiant.getNumEtudiant()).isEqualTo(UPDATED_NUM_ETUDIANT);
        assertThat(testEtudiant.getNumTel()).isEqualTo(DEFAULT_NUM_TEL);
        assertThat(testEtudiant.getDateDeNaissance()).isEqualTo(UPDATED_DATE_DE_NAISSANCE);
        assertThat(testEtudiant.getPhotoDeProfile()).isEqualTo(DEFAULT_PHOTO_DE_PROFILE);
        assertThat(testEtudiant.getPhotoDeProfileContentType()).isEqualTo(DEFAULT_PHOTO_DE_PROFILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateEtudiantWithPatch() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();

        // Update the etudiant using partial update
        Etudiant partialUpdatedEtudiant = new Etudiant();
        partialUpdatedEtudiant.setId(etudiant.getId());

        partialUpdatedEtudiant
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .numEtudiant(UPDATED_NUM_ETUDIANT)
            .numTel(UPDATED_NUM_TEL)
            .dateDeNaissance(UPDATED_DATE_DE_NAISSANCE)
            .photoDeProfile(UPDATED_PHOTO_DE_PROFILE)
            .photoDeProfileContentType(UPDATED_PHOTO_DE_PROFILE_CONTENT_TYPE);

        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEtudiant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEtudiant))
            )
            .andExpect(status().isOk());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeUpdate);
        Etudiant testEtudiant = etudiantList.get(etudiantList.size() - 1);
        assertThat(testEtudiant.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testEtudiant.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testEtudiant.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testEtudiant.getNumEtudiant()).isEqualTo(UPDATED_NUM_ETUDIANT);
        assertThat(testEtudiant.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
        assertThat(testEtudiant.getDateDeNaissance()).isEqualTo(UPDATED_DATE_DE_NAISSANCE);
        assertThat(testEtudiant.getPhotoDeProfile()).isEqualTo(UPDATED_PHOTO_DE_PROFILE);
        assertThat(testEtudiant.getPhotoDeProfileContentType()).isEqualTo(UPDATED_PHOTO_DE_PROFILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();
        etudiant.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, etudiant.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etudiant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();
        etudiant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(etudiant))
            )
            .andExpect(status().isBadRequest());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEtudiant() throws Exception {
        int databaseSizeBeforeUpdate = etudiantRepository.findAll().size();
        etudiant.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEtudiantMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(etudiant)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Etudiant in the database
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEtudiant() throws Exception {
        // Initialize the database
        etudiantRepository.saveAndFlush(etudiant);

        int databaseSizeBeforeDelete = etudiantRepository.findAll().size();

        // Delete the etudiant
        restEtudiantMockMvc
            .perform(delete(ENTITY_API_URL_ID, etudiant.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Etudiant> etudiantList = etudiantRepository.findAll();
        assertThat(etudiantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
