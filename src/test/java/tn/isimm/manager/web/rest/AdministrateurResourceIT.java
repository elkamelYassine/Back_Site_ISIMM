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
import tn.isimm.manager.domain.Administrateur;
import tn.isimm.manager.repository.AdministrateurRepository;

/**
 * Integration tests for the {@link AdministrateurResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AdministrateurResourceIT {

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_MATRICULE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE = "BBBBBBBBBB";

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_TEL = "AAAAAAAAAA";
    private static final String UPDATED_NUM_TEL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_DE_NAISSANCE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_DE_NAISSANCE = LocalDate.now(ZoneId.systemDefault());

    private static final byte[] DEFAULT_PHOTO_DE_PROFILE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO_DE_PROFILE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_DE_PROFILE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_DE_PROFILE_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/administrateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdministrateurRepository administrateurRepository;

    @Mock
    private AdministrateurRepository administrateurRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdministrateurMockMvc;

    private Administrateur administrateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrateur createEntity(EntityManager em) {
        Administrateur administrateur = new Administrateur()
            .prenom(DEFAULT_PRENOM)
            .nom(DEFAULT_NOM)
            .email(DEFAULT_EMAIL)
            .matricule(DEFAULT_MATRICULE)
            .titre(DEFAULT_TITRE)
            .numTel(DEFAULT_NUM_TEL)
            .dateDeNaissance(DEFAULT_DATE_DE_NAISSANCE)
            .photoDeProfile(DEFAULT_PHOTO_DE_PROFILE)
            .photoDeProfileContentType(DEFAULT_PHOTO_DE_PROFILE_CONTENT_TYPE);
        return administrateur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrateur createUpdatedEntity(EntityManager em) {
        Administrateur administrateur = new Administrateur()
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .matricule(UPDATED_MATRICULE)
            .titre(UPDATED_TITRE)
            .numTel(UPDATED_NUM_TEL)
            .dateDeNaissance(UPDATED_DATE_DE_NAISSANCE)
            .photoDeProfile(UPDATED_PHOTO_DE_PROFILE)
            .photoDeProfileContentType(UPDATED_PHOTO_DE_PROFILE_CONTENT_TYPE);
        return administrateur;
    }

    @BeforeEach
    public void initTest() {
        administrateur = createEntity(em);
    }

    @Test
    @Transactional
    void createAdministrateur() throws Exception {
        int databaseSizeBeforeCreate = administrateurRepository.findAll().size();
        // Create the Administrateur
        restAdministrateurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administrateur))
            )
            .andExpect(status().isCreated());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeCreate + 1);
        Administrateur testAdministrateur = administrateurList.get(administrateurList.size() - 1);
        assertThat(testAdministrateur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testAdministrateur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testAdministrateur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAdministrateur.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
        assertThat(testAdministrateur.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testAdministrateur.getNumTel()).isEqualTo(DEFAULT_NUM_TEL);
        assertThat(testAdministrateur.getDateDeNaissance()).isEqualTo(DEFAULT_DATE_DE_NAISSANCE);
        assertThat(testAdministrateur.getPhotoDeProfile()).isEqualTo(DEFAULT_PHOTO_DE_PROFILE);
        assertThat(testAdministrateur.getPhotoDeProfileContentType()).isEqualTo(DEFAULT_PHOTO_DE_PROFILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createAdministrateurWithExistingId() throws Exception {
        // Create the Administrateur with an existing ID
        administrateur.setId(1L);

        int databaseSizeBeforeCreate = administrateurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdministrateurMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administrateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAdministrateurs() throws Exception {
        // Initialize the database
        administrateurRepository.saveAndFlush(administrateur);

        // Get all the administrateurList
        restAdministrateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(administrateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE)))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].numTel").value(hasItem(DEFAULT_NUM_TEL)))
            .andExpect(jsonPath("$.[*].dateDeNaissance").value(hasItem(DEFAULT_DATE_DE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].photoDeProfileContentType").value(hasItem(DEFAULT_PHOTO_DE_PROFILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photoDeProfile").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PHOTO_DE_PROFILE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAdministrateursWithEagerRelationshipsIsEnabled() throws Exception {
        when(administrateurRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAdministrateurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(administrateurRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAdministrateursWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(administrateurRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAdministrateurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(administrateurRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAdministrateur() throws Exception {
        // Initialize the database
        administrateurRepository.saveAndFlush(administrateur);

        // Get the administrateur
        restAdministrateurMockMvc
            .perform(get(ENTITY_API_URL_ID, administrateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(administrateur.getId().intValue()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE))
            .andExpect(jsonPath("$.numTel").value(DEFAULT_NUM_TEL))
            .andExpect(jsonPath("$.dateDeNaissance").value(DEFAULT_DATE_DE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.photoDeProfileContentType").value(DEFAULT_PHOTO_DE_PROFILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.photoDeProfile").value(Base64.getEncoder().encodeToString(DEFAULT_PHOTO_DE_PROFILE)));
    }

    @Test
    @Transactional
    void getNonExistingAdministrateur() throws Exception {
        // Get the administrateur
        restAdministrateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdministrateur() throws Exception {
        // Initialize the database
        administrateurRepository.saveAndFlush(administrateur);

        int databaseSizeBeforeUpdate = administrateurRepository.findAll().size();

        // Update the administrateur
        Administrateur updatedAdministrateur = administrateurRepository.findById(administrateur.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAdministrateur are not directly saved in db
        em.detach(updatedAdministrateur);
        updatedAdministrateur
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .matricule(UPDATED_MATRICULE)
            .titre(UPDATED_TITRE)
            .numTel(UPDATED_NUM_TEL)
            .dateDeNaissance(UPDATED_DATE_DE_NAISSANCE)
            .photoDeProfile(UPDATED_PHOTO_DE_PROFILE)
            .photoDeProfileContentType(UPDATED_PHOTO_DE_PROFILE_CONTENT_TYPE);

        restAdministrateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAdministrateur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAdministrateur))
            )
            .andExpect(status().isOk());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeUpdate);
        Administrateur testAdministrateur = administrateurList.get(administrateurList.size() - 1);
        assertThat(testAdministrateur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testAdministrateur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAdministrateur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAdministrateur.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testAdministrateur.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testAdministrateur.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
        assertThat(testAdministrateur.getDateDeNaissance()).isEqualTo(UPDATED_DATE_DE_NAISSANCE);
        assertThat(testAdministrateur.getPhotoDeProfile()).isEqualTo(UPDATED_PHOTO_DE_PROFILE);
        assertThat(testAdministrateur.getPhotoDeProfileContentType()).isEqualTo(UPDATED_PHOTO_DE_PROFILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingAdministrateur() throws Exception {
        int databaseSizeBeforeUpdate = administrateurRepository.findAll().size();
        administrateur.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, administrateur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(administrateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdministrateur() throws Exception {
        int databaseSizeBeforeUpdate = administrateurRepository.findAll().size();
        administrateur.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(administrateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdministrateur() throws Exception {
        int databaseSizeBeforeUpdate = administrateurRepository.findAll().size();
        administrateur.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(administrateur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdministrateurWithPatch() throws Exception {
        // Initialize the database
        administrateurRepository.saveAndFlush(administrateur);

        int databaseSizeBeforeUpdate = administrateurRepository.findAll().size();

        // Update the administrateur using partial update
        Administrateur partialUpdatedAdministrateur = new Administrateur();
        partialUpdatedAdministrateur.setId(administrateur.getId());

        partialUpdatedAdministrateur
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .matricule(UPDATED_MATRICULE)
            .numTel(UPDATED_NUM_TEL)
            .dateDeNaissance(UPDATED_DATE_DE_NAISSANCE)
            .photoDeProfile(UPDATED_PHOTO_DE_PROFILE)
            .photoDeProfileContentType(UPDATED_PHOTO_DE_PROFILE_CONTENT_TYPE);

        restAdministrateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdministrateur))
            )
            .andExpect(status().isOk());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeUpdate);
        Administrateur testAdministrateur = administrateurList.get(administrateurList.size() - 1);
        assertThat(testAdministrateur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testAdministrateur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAdministrateur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testAdministrateur.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testAdministrateur.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testAdministrateur.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
        assertThat(testAdministrateur.getDateDeNaissance()).isEqualTo(UPDATED_DATE_DE_NAISSANCE);
        assertThat(testAdministrateur.getPhotoDeProfile()).isEqualTo(UPDATED_PHOTO_DE_PROFILE);
        assertThat(testAdministrateur.getPhotoDeProfileContentType()).isEqualTo(UPDATED_PHOTO_DE_PROFILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateAdministrateurWithPatch() throws Exception {
        // Initialize the database
        administrateurRepository.saveAndFlush(administrateur);

        int databaseSizeBeforeUpdate = administrateurRepository.findAll().size();

        // Update the administrateur using partial update
        Administrateur partialUpdatedAdministrateur = new Administrateur();
        partialUpdatedAdministrateur.setId(administrateur.getId());

        partialUpdatedAdministrateur
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .matricule(UPDATED_MATRICULE)
            .titre(UPDATED_TITRE)
            .numTel(UPDATED_NUM_TEL)
            .dateDeNaissance(UPDATED_DATE_DE_NAISSANCE)
            .photoDeProfile(UPDATED_PHOTO_DE_PROFILE)
            .photoDeProfileContentType(UPDATED_PHOTO_DE_PROFILE_CONTENT_TYPE);

        restAdministrateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdministrateur))
            )
            .andExpect(status().isOk());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeUpdate);
        Administrateur testAdministrateur = administrateurList.get(administrateurList.size() - 1);
        assertThat(testAdministrateur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testAdministrateur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testAdministrateur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testAdministrateur.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testAdministrateur.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testAdministrateur.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
        assertThat(testAdministrateur.getDateDeNaissance()).isEqualTo(UPDATED_DATE_DE_NAISSANCE);
        assertThat(testAdministrateur.getPhotoDeProfile()).isEqualTo(UPDATED_PHOTO_DE_PROFILE);
        assertThat(testAdministrateur.getPhotoDeProfileContentType()).isEqualTo(UPDATED_PHOTO_DE_PROFILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingAdministrateur() throws Exception {
        int databaseSizeBeforeUpdate = administrateurRepository.findAll().size();
        administrateur.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, administrateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(administrateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdministrateur() throws Exception {
        int databaseSizeBeforeUpdate = administrateurRepository.findAll().size();
        administrateur.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(administrateur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdministrateur() throws Exception {
        int databaseSizeBeforeUpdate = administrateurRepository.findAll().size();
        administrateur.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministrateurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(administrateur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrateur in the database
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdministrateur() throws Exception {
        // Initialize the database
        administrateurRepository.saveAndFlush(administrateur);

        int databaseSizeBeforeDelete = administrateurRepository.findAll().size();

        // Delete the administrateur
        restAdministrateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, administrateur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Administrateur> administrateurList = administrateurRepository.findAll();
        assertThat(administrateurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
