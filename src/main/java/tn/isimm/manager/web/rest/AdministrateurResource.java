package tn.isimm.manager.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import tn.isimm.manager.domain.Administrateur;
import tn.isimm.manager.repository.AdministrateurRepository;
import tn.isimm.manager.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.isimm.manager.domain.Administrateur}.
 */
@RestController
@RequestMapping("/api/administrateurs")
@Transactional
public class AdministrateurResource {

    private final Logger log = LoggerFactory.getLogger(AdministrateurResource.class);

    private static final String ENTITY_NAME = "administrateur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdministrateurRepository administrateurRepository;

    public AdministrateurResource(AdministrateurRepository administrateurRepository) {
        this.administrateurRepository = administrateurRepository;
    }

    /**
     * {@code POST  /administrateurs} : Create a new administrateur.
     *
     * @param administrateur the administrateur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new administrateur, or with status {@code 400 (Bad Request)} if the administrateur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Administrateur> createAdministrateur(@RequestBody Administrateur administrateur) throws URISyntaxException {
        log.debug("REST request to save Administrateur : {}", administrateur);
        if (administrateur.getId() != null) {
            throw new BadRequestAlertException("A new administrateur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Administrateur result = administrateurRepository.save(administrateur);
        return ResponseEntity
            .created(new URI("/api/administrateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /administrateurs/:id} : Updates an existing administrateur.
     *
     * @param id the id of the administrateur to save.
     * @param administrateur the administrateur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated administrateur,
     * or with status {@code 400 (Bad Request)} if the administrateur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the administrateur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Administrateur> updateAdministrateur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Administrateur administrateur
    ) throws URISyntaxException {
        log.debug("REST request to update Administrateur : {}, {}", id, administrateur);
        if (administrateur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, administrateur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!administrateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Administrateur result = administrateurRepository.save(administrateur);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, administrateur.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /administrateurs/:id} : Partial updates given fields of an existing administrateur, field will ignore if it is null
     *
     * @param id the id of the administrateur to save.
     * @param administrateur the administrateur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated administrateur,
     * or with status {@code 400 (Bad Request)} if the administrateur is not valid,
     * or with status {@code 404 (Not Found)} if the administrateur is not found,
     * or with status {@code 500 (Internal Server Error)} if the administrateur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Administrateur> partialUpdateAdministrateur(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Administrateur administrateur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Administrateur partially : {}, {}", id, administrateur);
        if (administrateur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, administrateur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!administrateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Administrateur> result = administrateurRepository
            .findById(administrateur.getId())
            .map(existingAdministrateur -> {
                if (administrateur.getPrenom() != null) {
                    existingAdministrateur.setPrenom(administrateur.getPrenom());
                }
                if (administrateur.getNom() != null) {
                    existingAdministrateur.setNom(administrateur.getNom());
                }
                if (administrateur.getEmail() != null) {
                    existingAdministrateur.setEmail(administrateur.getEmail());
                }
                if (administrateur.getMatricule() != null) {
                    existingAdministrateur.setMatricule(administrateur.getMatricule());
                }
                if (administrateur.getTitre() != null) {
                    existingAdministrateur.setTitre(administrateur.getTitre());
                }
                if (administrateur.getNumTel() != null) {
                    existingAdministrateur.setNumTel(administrateur.getNumTel());
                }
                if (administrateur.getDateDeNaissance() != null) {
                    existingAdministrateur.setDateDeNaissance(administrateur.getDateDeNaissance());
                }
                if (administrateur.getPhotoDeProfile() != null) {
                    existingAdministrateur.setPhotoDeProfile(administrateur.getPhotoDeProfile());
                }
                if (administrateur.getPhotoDeProfileContentType() != null) {
                    existingAdministrateur.setPhotoDeProfileContentType(administrateur.getPhotoDeProfileContentType());
                }

                return existingAdministrateur;
            })
            .map(administrateurRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, administrateur.getId().toString())
        );
    }

    /**
     * {@code GET  /administrateurs} : get all the administrateurs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of administrateurs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<Administrateur>> getAllAdministrateurs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Administrateurs");
        Page<Administrateur> page = administrateurRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /administrateurs/:id} : get the "id" administrateur.
     *
     * @param id the id of the administrateur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the administrateur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Administrateur> getAdministrateur(@PathVariable("id") Long id) {
        log.debug("REST request to get Administrateur : {}", id);
        Optional<Administrateur> administrateur = administrateurRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(administrateur);
    }

    /**
     * {@code DELETE  /administrateurs/:id} : delete the "id" administrateur.
     *
     * @param id the id of the administrateur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrateur(@PathVariable("id") Long id) {
        log.debug("REST request to delete Administrateur : {}", id);
        administrateurRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
