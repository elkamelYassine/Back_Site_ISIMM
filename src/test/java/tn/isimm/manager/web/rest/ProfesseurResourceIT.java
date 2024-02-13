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
import tn.isimm.manager.domain.Professeur;
import tn.isimm.manager.domain.enumeration.Departement;
import tn.isimm.manager.repository.ProfesseurRepository;

/**
 * Integration tests for the {@link ProfesseurResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProfesseurResourceIT {

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_MATRICULE = "AAAAAAAAAA";
    private static final String UPDATED_MATRICULE = "BBBBBBBBBB";

    private static final Departement DEFAULT_DEPARTEMENT = Departement.Informatique;
    private static final Departement UPDATED_DEPARTEMENT = Departement.Mathematique;

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

    private static final String ENTITY_API_URL = "/api/professeurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProfesseurRepository professeurRepository;

    @Mock
    private ProfesseurRepository professeurRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfesseurMockMvc;

    private Professeur professeur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Professeur createEntity(EntityManager em) {
        Professeur professeur = new Professeur()
            .prenom(DEFAULT_PRENOM)
            .nom(DEFAULT_NOM)
            .email(DEFAULT_EMAIL)
            .matricule(DEFAULT_MATRICULE)
            .departement(DEFAULT_DEPARTEMENT)
            .titre(DEFAULT_TITRE)
            .numTel(DEFAULT_NUM_TEL)
            .dateDeNaissance(DEFAULT_DATE_DE_NAISSANCE)
            .photoDeProfile(DEFAULT_PHOTO_DE_PROFILE)
            .photoDeProfileContentType(DEFAULT_PHOTO_DE_PROFILE_CONTENT_TYPE);
        return professeur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Professeur createUpdatedEntity(EntityManager em) {
        Professeur professeur = new Professeur()
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .matricule(UPDATED_MATRICULE)
            .departement(UPDATED_DEPARTEMENT)
            .titre(UPDATED_TITRE)
            .numTel(UPDATED_NUM_TEL)
            .dateDeNaissance(UPDATED_DATE_DE_NAISSANCE)
            .photoDeProfile(UPDATED_PHOTO_DE_PROFILE)
            .photoDeProfileContentType(UPDATED_PHOTO_DE_PROFILE_CONTENT_TYPE);
        return professeur;
    }

    @BeforeEach
    public void initTest() {
        professeur = createEntity(em);
    }

    @Test
    @Transactional
    void createProfesseur() throws Exception {
        int databaseSizeBeforeCreate = professeurRepository.findAll().size();
        // Create the Professeur
        restProfesseurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professeur)))
            .andExpect(status().isCreated());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeCreate + 1);
        Professeur testProfesseur = professeurList.get(professeurList.size() - 1);
        assertThat(testProfesseur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testProfesseur.getNom()).isEqualTo(DEFAULT_NOM);
        assertThat(testProfesseur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testProfesseur.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
        assertThat(testProfesseur.getDepartement()).isEqualTo(DEFAULT_DEPARTEMENT);
        assertThat(testProfesseur.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testProfesseur.getNumTel()).isEqualTo(DEFAULT_NUM_TEL);
        assertThat(testProfesseur.getDateDeNaissance()).isEqualTo(DEFAULT_DATE_DE_NAISSANCE);
        assertThat(testProfesseur.getPhotoDeProfile()).isEqualTo(DEFAULT_PHOTO_DE_PROFILE);
        assertThat(testProfesseur.getPhotoDeProfileContentType()).isEqualTo(DEFAULT_PHOTO_DE_PROFILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createProfesseurWithExistingId() throws Exception {
        // Create the Professeur with an existing ID
        professeur.setId(1L);

        int databaseSizeBeforeCreate = professeurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfesseurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professeur)))
            .andExpect(status().isBadRequest());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProfesseurs() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get all the professeurList
        restProfesseurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(professeur.getId().intValue())))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].matricule").value(hasItem(DEFAULT_MATRICULE)))
            .andExpect(jsonPath("$.[*].departement").value(hasItem(DEFAULT_DEPARTEMENT.toString())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].numTel").value(hasItem(DEFAULT_NUM_TEL)))
            .andExpect(jsonPath("$.[*].dateDeNaissance").value(hasItem(DEFAULT_DATE_DE_NAISSANCE.toString())))
            .andExpect(jsonPath("$.[*].photoDeProfileContentType").value(hasItem(DEFAULT_PHOTO_DE_PROFILE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photoDeProfile").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_PHOTO_DE_PROFILE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfesseursWithEagerRelationshipsIsEnabled() throws Exception {
        when(professeurRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfesseurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(professeurRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProfesseursWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(professeurRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProfesseurMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(professeurRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProfesseur() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        // Get the professeur
        restProfesseurMockMvc
            .perform(get(ENTITY_API_URL_ID, professeur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(professeur.getId().intValue()))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.matricule").value(DEFAULT_MATRICULE))
            .andExpect(jsonPath("$.departement").value(DEFAULT_DEPARTEMENT.toString()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE))
            .andExpect(jsonPath("$.numTel").value(DEFAULT_NUM_TEL))
            .andExpect(jsonPath("$.dateDeNaissance").value(DEFAULT_DATE_DE_NAISSANCE.toString()))
            .andExpect(jsonPath("$.photoDeProfileContentType").value(DEFAULT_PHOTO_DE_PROFILE_CONTENT_TYPE))
            .andExpect(jsonPath("$.photoDeProfile").value(Base64.getEncoder().encodeToString(DEFAULT_PHOTO_DE_PROFILE)));
    }

    @Test
    @Transactional
    void getNonExistingProfesseur() throws Exception {
        // Get the professeur
        restProfesseurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfesseur() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        int databaseSizeBeforeUpdate = professeurRepository.findAll().size();

        // Update the professeur
        Professeur updatedProfesseur = professeurRepository.findById(professeur.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProfesseur are not directly saved in db
        em.detach(updatedProfesseur);
        updatedProfesseur
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .matricule(UPDATED_MATRICULE)
            .departement(UPDATED_DEPARTEMENT)
            .titre(UPDATED_TITRE)
            .numTel(UPDATED_NUM_TEL)
            .dateDeNaissance(UPDATED_DATE_DE_NAISSANCE)
            .photoDeProfile(UPDATED_PHOTO_DE_PROFILE)
            .photoDeProfileContentType(UPDATED_PHOTO_DE_PROFILE_CONTENT_TYPE);

        restProfesseurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProfesseur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProfesseur))
            )
            .andExpect(status().isOk());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeUpdate);
        Professeur testProfesseur = professeurList.get(professeurList.size() - 1);
        assertThat(testProfesseur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testProfesseur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testProfesseur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testProfesseur.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testProfesseur.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testProfesseur.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testProfesseur.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
        assertThat(testProfesseur.getDateDeNaissance()).isEqualTo(UPDATED_DATE_DE_NAISSANCE);
        assertThat(testProfesseur.getPhotoDeProfile()).isEqualTo(UPDATED_PHOTO_DE_PROFILE);
        assertThat(testProfesseur.getPhotoDeProfileContentType()).isEqualTo(UPDATED_PHOTO_DE_PROFILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingProfesseur() throws Exception {
        int databaseSizeBeforeUpdate = professeurRepository.findAll().size();
        professeur.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfesseurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, professeur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfesseur() throws Exception {
        int databaseSizeBeforeUpdate = professeurRepository.findAll().size();
        professeur.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfesseurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(professeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfesseur() throws Exception {
        int databaseSizeBeforeUpdate = professeurRepository.findAll().size();
        professeur.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfesseurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(professeur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfesseurWithPatch() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        int databaseSizeBeforeUpdate = professeurRepository.findAll().size();

        // Update the professeur using partial update
        Professeur partialUpdatedProfesseur = new Professeur();
        partialUpdatedProfesseur.setId(professeur.getId());

        partialUpdatedProfesseur
            .nom(UPDATED_NOM)
            .departement(UPDATED_DEPARTEMENT)
            .numTel(UPDATED_NUM_TEL)
            .photoDeProfile(UPDATED_PHOTO_DE_PROFILE)
            .photoDeProfileContentType(UPDATED_PHOTO_DE_PROFILE_CONTENT_TYPE);

        restProfesseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfesseur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProfesseur))
            )
            .andExpect(status().isOk());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeUpdate);
        Professeur testProfesseur = professeurList.get(professeurList.size() - 1);
        assertThat(testProfesseur.getPrenom()).isEqualTo(DEFAULT_PRENOM);
        assertThat(testProfesseur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testProfesseur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testProfesseur.getMatricule()).isEqualTo(DEFAULT_MATRICULE);
        assertThat(testProfesseur.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testProfesseur.getTitre()).isEqualTo(DEFAULT_TITRE);
        assertThat(testProfesseur.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
        assertThat(testProfesseur.getDateDeNaissance()).isEqualTo(DEFAULT_DATE_DE_NAISSANCE);
        assertThat(testProfesseur.getPhotoDeProfile()).isEqualTo(UPDATED_PHOTO_DE_PROFILE);
        assertThat(testProfesseur.getPhotoDeProfileContentType()).isEqualTo(UPDATED_PHOTO_DE_PROFILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateProfesseurWithPatch() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        int databaseSizeBeforeUpdate = professeurRepository.findAll().size();

        // Update the professeur using partial update
        Professeur partialUpdatedProfesseur = new Professeur();
        partialUpdatedProfesseur.setId(professeur.getId());

        partialUpdatedProfesseur
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .email(UPDATED_EMAIL)
            .matricule(UPDATED_MATRICULE)
            .departement(UPDATED_DEPARTEMENT)
            .titre(UPDATED_TITRE)
            .numTel(UPDATED_NUM_TEL)
            .dateDeNaissance(UPDATED_DATE_DE_NAISSANCE)
            .photoDeProfile(UPDATED_PHOTO_DE_PROFILE)
            .photoDeProfileContentType(UPDATED_PHOTO_DE_PROFILE_CONTENT_TYPE);

        restProfesseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfesseur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProfesseur))
            )
            .andExpect(status().isOk());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeUpdate);
        Professeur testProfesseur = professeurList.get(professeurList.size() - 1);
        assertThat(testProfesseur.getPrenom()).isEqualTo(UPDATED_PRENOM);
        assertThat(testProfesseur.getNom()).isEqualTo(UPDATED_NOM);
        assertThat(testProfesseur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testProfesseur.getMatricule()).isEqualTo(UPDATED_MATRICULE);
        assertThat(testProfesseur.getDepartement()).isEqualTo(UPDATED_DEPARTEMENT);
        assertThat(testProfesseur.getTitre()).isEqualTo(UPDATED_TITRE);
        assertThat(testProfesseur.getNumTel()).isEqualTo(UPDATED_NUM_TEL);
        assertThat(testProfesseur.getDateDeNaissance()).isEqualTo(UPDATED_DATE_DE_NAISSANCE);
        assertThat(testProfesseur.getPhotoDeProfile()).isEqualTo(UPDATED_PHOTO_DE_PROFILE);
        assertThat(testProfesseur.getPhotoDeProfileContentType()).isEqualTo(UPDATED_PHOTO_DE_PROFILE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingProfesseur() throws Exception {
        int databaseSizeBeforeUpdate = professeurRepository.findAll().size();
        professeur.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfesseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, professeur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(professeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfesseur() throws Exception {
        int databaseSizeBeforeUpdate = professeurRepository.findAll().size();
        professeur.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfesseurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(professeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfesseur() throws Exception {
        int databaseSizeBeforeUpdate = professeurRepository.findAll().size();
        professeur.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfesseurMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(professeur))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Professeur in the database
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfesseur() throws Exception {
        // Initialize the database
        professeurRepository.saveAndFlush(professeur);

        int databaseSizeBeforeDelete = professeurRepository.findAll().size();

        // Delete the professeur
        restProfesseurMockMvc
            .perform(delete(ENTITY_API_URL_ID, professeur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Professeur> professeurList = professeurRepository.findAll();
        assertThat(professeurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
