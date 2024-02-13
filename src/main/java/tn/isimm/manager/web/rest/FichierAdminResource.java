package tn.isimm.manager.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import tn.isimm.manager.domain.FichierAdmin;
import tn.isimm.manager.repository.FichierAdminRepository;
import tn.isimm.manager.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.isimm.manager.domain.FichierAdmin}.
 */
@RestController
@RequestMapping("/api/fichier-admins")
@Transactional
public class FichierAdminResource {

    private final Logger log = LoggerFactory.getLogger(FichierAdminResource.class);

    private static final String ENTITY_NAME = "fichierAdmin";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FichierAdminRepository fichierAdminRepository;

    public FichierAdminResource(FichierAdminRepository fichierAdminRepository) {
        this.fichierAdminRepository = fichierAdminRepository;
    }

    /**
     * {@code POST  /fichier-admins} : Create a new fichierAdmin.
     *
     * @param fichierAdmin the fichierAdmin to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fichierAdmin, or with status {@code 400 (Bad Request)} if the fichierAdmin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FichierAdmin> createFichierAdmin(@RequestBody FichierAdmin fichierAdmin) throws URISyntaxException {
        log.debug("REST request to save FichierAdmin : {}", fichierAdmin);
        if (fichierAdmin.getId() != null) {
            throw new BadRequestAlertException("A new fichierAdmin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FichierAdmin result = fichierAdminRepository.save(fichierAdmin);
        return ResponseEntity
            .created(new URI("/api/fichier-admins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fichier-admins/:id} : Updates an existing fichierAdmin.
     *
     * @param id the id of the fichierAdmin to save.
     * @param fichierAdmin the fichierAdmin to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fichierAdmin,
     * or with status {@code 400 (Bad Request)} if the fichierAdmin is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fichierAdmin couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FichierAdmin> updateFichierAdmin(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FichierAdmin fichierAdmin
    ) throws URISyntaxException {
        log.debug("REST request to update FichierAdmin : {}, {}", id, fichierAdmin);
        if (fichierAdmin.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fichierAdmin.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fichierAdminRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FichierAdmin result = fichierAdminRepository.save(fichierAdmin);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fichierAdmin.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fichier-admins/:id} : Partial updates given fields of an existing fichierAdmin, field will ignore if it is null
     *
     * @param id the id of the fichierAdmin to save.
     * @param fichierAdmin the fichierAdmin to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fichierAdmin,
     * or with status {@code 400 (Bad Request)} if the fichierAdmin is not valid,
     * or with status {@code 404 (Not Found)} if the fichierAdmin is not found,
     * or with status {@code 500 (Internal Server Error)} if the fichierAdmin couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FichierAdmin> partialUpdateFichierAdmin(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FichierAdmin fichierAdmin
    ) throws URISyntaxException {
        log.debug("REST request to partial update FichierAdmin partially : {}, {}", id, fichierAdmin);
        if (fichierAdmin.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fichierAdmin.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fichierAdminRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FichierAdmin> result = fichierAdminRepository
            .findById(fichierAdmin.getId())
            .map(existingFichierAdmin -> {
                if (fichierAdmin.getTitre() != null) {
                    existingFichierAdmin.setTitre(fichierAdmin.getTitre());
                }
                if (fichierAdmin.getType() != null) {
                    existingFichierAdmin.setType(fichierAdmin.getType());
                }
                if (fichierAdmin.getFile() != null) {
                    existingFichierAdmin.setFile(fichierAdmin.getFile());
                }
                if (fichierAdmin.getFileContentType() != null) {
                    existingFichierAdmin.setFileContentType(fichierAdmin.getFileContentType());
                }
                if (fichierAdmin.getDemandeValide() != null) {
                    existingFichierAdmin.setDemandeValide(fichierAdmin.getDemandeValide());
                }

                return existingFichierAdmin;
            })
            .map(fichierAdminRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fichierAdmin.getId().toString())
        );
    }

    /**
     * {@code GET  /fichier-admins} : get all the fichierAdmins.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fichierAdmins in body.
     */
    @GetMapping("")
    public List<FichierAdmin> getAllFichierAdmins() {
        log.debug("REST request to get all FichierAdmins");
        return fichierAdminRepository.findAll();
    }

    /**
     * {@code GET  /fichier-admins/:id} : get the "id" fichierAdmin.
     *
     * @param id the id of the fichierAdmin to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fichierAdmin, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FichierAdmin> getFichierAdmin(@PathVariable("id") Long id) {
        log.debug("REST request to get FichierAdmin : {}", id);
        Optional<FichierAdmin> fichierAdmin = fichierAdminRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(fichierAdmin);
    }

    /**
     * {@code DELETE  /fichier-admins/:id} : delete the "id" fichierAdmin.
     *
     * @param id the id of the fichierAdmin to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFichierAdmin(@PathVariable("id") Long id) {
        log.debug("REST request to delete FichierAdmin : {}", id);
        fichierAdminRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
