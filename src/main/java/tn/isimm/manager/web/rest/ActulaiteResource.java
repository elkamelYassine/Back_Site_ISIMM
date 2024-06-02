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
import tn.isimm.manager.domain.Actulaite;
import tn.isimm.manager.repository.ActulaiteRepository;
import tn.isimm.manager.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.isimm.manager.domain.Actulaite}.
 */
@RestController
@RequestMapping("/api/actulaites")
@Transactional
public class ActulaiteResource {

    private final Logger log = LoggerFactory.getLogger(ActulaiteResource.class);

    private static final String ENTITY_NAME = "actulaite";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActulaiteRepository actulaiteRepository;

    public ActulaiteResource(ActulaiteRepository actulaiteRepository) {
        this.actulaiteRepository = actulaiteRepository;
    }

    /**
     * {@code POST  /actulaites} : Create a new actulaite.
     *
     * @param actulaite the actulaite to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new actulaite, or with status {@code 400 (Bad Request)} if the actulaite has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Actulaite> createActulaite(@RequestBody Actulaite actulaite) throws URISyntaxException {
        log.debug("REST request to save Actulaite : {}", actulaite);
        if (actulaite.getId() != null) {
            throw new BadRequestAlertException("A new actulaite cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Actulaite result = actulaiteRepository.save(actulaite);
        return ResponseEntity
            .created(new URI("/api/actulaites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /actulaites/:id} : Updates an existing actulaite.
     *
     * @param id the id of the actulaite to save.
     * @param actulaite the actulaite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated actulaite,
     * or with status {@code 400 (Bad Request)} if the actulaite is not valid,
     * or with status {@code 500 (Internal Server Error)} if the actulaite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Actulaite> updateActulaite(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Actulaite actulaite
    ) throws URISyntaxException {
        log.debug("REST request to update Actulaite : {}, {}", id, actulaite);
        if (actulaite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, actulaite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!actulaiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Actulaite result = actulaiteRepository.save(actulaite);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, actulaite.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /actulaites/:id} : Partial updates given fields of an existing actulaite, field will ignore if it is null
     *
     * @param id the id of the actulaite to save.
     * @param actulaite the actulaite to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated actulaite,
     * or with status {@code 400 (Bad Request)} if the actulaite is not valid,
     * or with status {@code 404 (Not Found)} if the actulaite is not found,
     * or with status {@code 500 (Internal Server Error)} if the actulaite couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Actulaite> partialUpdateActulaite(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Actulaite actulaite
    ) throws URISyntaxException {
        log.debug("REST request to partial update Actulaite partially : {}, {}", id, actulaite);
        if (actulaite.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, actulaite.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!actulaiteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Actulaite> result = actulaiteRepository
            .findById(actulaite.getId())
            .map(existingActulaite -> {
                if (actulaite.getDate() != null) {
                    existingActulaite.setDate(actulaite.getDate());
                }
                if (actulaite.getData() != null) {
                    existingActulaite.setData(actulaite.getData());
                }
                if (actulaite.getImage() != null) {
                    existingActulaite.setImage(actulaite.getImage());
                }
                if (actulaite.getImageContentType() != null) {
                    existingActulaite.setImageContentType(actulaite.getImageContentType());
                }
                if (actulaite.getTitle() != null) {
                    existingActulaite.setTitle(actulaite.getTitle());
                }
                if (actulaite.getDescription() != null) {
                    existingActulaite.setDescription(actulaite.getDescription());
                }

                return existingActulaite;
            })
            .map(actulaiteRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, actulaite.getId().toString())
        );
    }

    /**
     * {@code GET  /actulaites} : get all the actulaites.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of actulaites in body.
     */
    @GetMapping("")
    public List<Actulaite> getAllActulaites() {
        log.debug("REST request to get all Actulaites");
        return actulaiteRepository.findAll();
    }

    /**
     * {@code GET  /actulaites/:id} : get the "id" actulaite.
     *
     * @param id the id of the actulaite to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the actulaite, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Actulaite> getActulaite(@PathVariable("id") Long id) {
        log.debug("REST request to get Actulaite : {}", id);
        Optional<Actulaite> actulaite = actulaiteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(actulaite);
    }

    /**
     * {@code DELETE  /actulaites/:id} : delete the "id" actulaite.
     *
     * @param id the id of the actulaite to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActulaite(@PathVariable("id") Long id) {
        log.debug("REST request to delete Actulaite : {}", id);
        actulaiteRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
