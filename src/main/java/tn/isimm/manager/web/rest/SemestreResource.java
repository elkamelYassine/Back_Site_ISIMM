package tn.isimm.manager.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import tn.isimm.manager.domain.Semestre;
import tn.isimm.manager.repository.SemestreRepository;
import tn.isimm.manager.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.isimm.manager.domain.Semestre}.
 */
@RestController
@RequestMapping("/api/semestres")
@Transactional
public class SemestreResource {

    private final Logger log = LoggerFactory.getLogger(SemestreResource.class);

    private static final String ENTITY_NAME = "semestre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SemestreRepository semestreRepository;

    public SemestreResource(SemestreRepository semestreRepository) {
        this.semestreRepository = semestreRepository;
    }

    /**
     * {@code POST  /semestres} : Create a new semestre.
     *
     * @param semestre the semestre to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new semestre, or with status {@code 400 (Bad Request)} if the semestre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Semestre> createSemestre(@RequestBody Semestre semestre) throws URISyntaxException {
        log.debug("REST request to save Semestre : {}", semestre);
        if (semestre.getId() != null) {
            throw new BadRequestAlertException("A new semestre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Semestre result = semestreRepository.save(semestre);
        return ResponseEntity
            .created(new URI("/api/semestres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /semestres/:id} : Updates an existing semestre.
     *
     * @param id the id of the semestre to save.
     * @param semestre the semestre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated semestre,
     * or with status {@code 400 (Bad Request)} if the semestre is not valid,
     * or with status {@code 500 (Internal Server Error)} if the semestre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Semestre> updateSemestre(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Semestre semestre
    ) throws URISyntaxException {
        log.debug("REST request to update Semestre : {}, {}", id, semestre);
        if (semestre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, semestre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!semestreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Semestre result = semestreRepository.save(semestre);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, semestre.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /semestres/:id} : Partial updates given fields of an existing semestre, field will ignore if it is null
     *
     * @param id the id of the semestre to save.
     * @param semestre the semestre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated semestre,
     * or with status {@code 400 (Bad Request)} if the semestre is not valid,
     * or with status {@code 404 (Not Found)} if the semestre is not found,
     * or with status {@code 500 (Internal Server Error)} if the semestre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Semestre> partialUpdateSemestre(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Semestre semestre
    ) throws URISyntaxException {
        log.debug("REST request to partial update Semestre partially : {}, {}", id, semestre);
        if (semestre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, semestre.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!semestreRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Semestre> result = semestreRepository
            .findById(semestre.getId())
            .map(existingSemestre -> {
                if (semestre.getAnneeScolaire() != null) {
                    existingSemestre.setAnneeScolaire(semestre.getAnneeScolaire());
                }
                if (semestre.getS() != null) {
                    existingSemestre.setS(semestre.getS());
                }

                return existingSemestre;
            })
            .map(semestreRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, semestre.getId().toString())
        );
    }

    /**
     * {@code GET  /semestres} : get all the semestres.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of semestres in body.
     */
    @GetMapping("")
    public List<Semestre> getAllSemestres(@RequestParam(name = "filter", required = false) String filter) {
        if ("niveau-is-null".equals(filter)) {
            log.debug("REST request to get all Semestres where niveau is null");
            return StreamSupport
                .stream(semestreRepository.findAll().spliterator(), false)
                .filter(semestre -> semestre.getNiveau() == null)
                .toList();
        }

        if ("matiere-is-null".equals(filter)) {
            log.debug("REST request to get all Semestres where matiere is null");
            return StreamSupport
                .stream(semestreRepository.findAll().spliterator(), false)
                .filter(semestre -> semestre.getMatiere() == null)
                .toList();
        }
        log.debug("REST request to get all Semestres");
        return semestreRepository.findAll();
    }

    /**
     * {@code GET  /semestres/:id} : get the "id" semestre.
     *
     * @param id the id of the semestre to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the semestre, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Semestre> getSemestre(@PathVariable("id") Long id) {
        log.debug("REST request to get Semestre : {}", id);
        Optional<Semestre> semestre = semestreRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(semestre);
    }

    /**
     * {@code DELETE  /semestres/:id} : delete the "id" semestre.
     *
     * @param id the id of the semestre to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSemestre(@PathVariable("id") Long id) {
        log.debug("REST request to delete Semestre : {}", id);
        semestreRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
