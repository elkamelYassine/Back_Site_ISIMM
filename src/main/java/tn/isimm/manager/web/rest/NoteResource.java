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
import tn.isimm.manager.domain.Note;
import tn.isimm.manager.repository.NoteRepository;
import tn.isimm.manager.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tn.isimm.manager.domain.Note}.
 */
@RestController
@RequestMapping("/api/notes")
@Transactional
public class NoteResource {

    private final Logger log = LoggerFactory.getLogger(NoteResource.class);

    private static final String ENTITY_NAME = "note";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NoteRepository noteRepository;

    public NoteResource(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * {@code POST  /notes} : Create a new note.
     *
     * @param note the note to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new note, or with status {@code 400 (Bad Request)} if the note has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Note> createNote(@RequestBody Note note) throws URISyntaxException {
        log.debug("REST request to save Note : {}", note);
        if (note.getId() != null) {
            throw new BadRequestAlertException("A new note cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Note result = noteRepository.save(note);
        return ResponseEntity
            .created(new URI("/api/notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /notes/:id} : Updates an existing note.
     *
     * @param id the id of the note to save.
     * @param note the note to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated note,
     * or with status {@code 400 (Bad Request)} if the note is not valid,
     * or with status {@code 500 (Internal Server Error)} if the note couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable(value = "id", required = false) final Long id, @RequestBody Note note)
        throws URISyntaxException {
        log.debug("REST request to update Note : {}, {}", id, note);
        if (note.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, note.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!noteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Note result = noteRepository.save(note);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, note.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /notes/:id} : Partial updates given fields of an existing note, field will ignore if it is null
     *
     * @param id the id of the note to save.
     * @param note the note to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated note,
     * or with status {@code 400 (Bad Request)} if the note is not valid,
     * or with status {@code 404 (Not Found)} if the note is not found,
     * or with status {@code 500 (Internal Server Error)} if the note couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Note> partialUpdateNote(@PathVariable(value = "id", required = false) final Long id, @RequestBody Note note)
        throws URISyntaxException {
        log.debug("REST request to partial update Note partially : {}, {}", id, note);
        if (note.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, note.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!noteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Note> result = noteRepository
            .findById(note.getId())
            .map(existingNote -> {
                if (note.getNote() != null) {
                    existingNote.setNote(note.getNote());
                }

                return existingNote;
            })
            .map(noteRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, note.getId().toString())
        );
    }

    /**
     * {@code GET  /notes} : get all the notes.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notes in body.
     */
    @GetMapping("")
    public List<Note> getAllNotes(@RequestParam(name = "filter", required = false) String filter) {
        if ("matiere-is-null".equals(filter)) {
            log.debug("REST request to get all Notes where matiere is null");
            return StreamSupport.stream(noteRepository.findAll().spliterator(), false).filter(note -> note.getMatiere() == null).toList();
        }
        log.debug("REST request to get all Notes");
        return noteRepository.findAll();
    }

    /**
     * {@code GET  /notes/:id} : get the "id" note.
     *
     * @param id the id of the note to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the note, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Note> getNote(@PathVariable("id") Long id) {
        log.debug("REST request to get Note : {}", id);
        Optional<Note> note = noteRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(note);
    }

    /**
     * {@code DELETE  /notes/:id} : delete the "id" note.
     *
     * @param id the id of the note to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable("id") Long id) {
        log.debug("REST request to delete Note : {}", id);
        noteRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
