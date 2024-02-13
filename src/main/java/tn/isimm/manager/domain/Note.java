package tn.isimm.manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Note.
 */
@Entity
@Table(name = "note")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Note implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "note")
    private Integer note;

    @JsonIgnoreProperties(value = { "semestre", "note", "cours", "seance", "professeurs" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "note")
    private Matiere matiere;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Note id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNote() {
        return this.note;
    }

    public Note note(Integer note) {
        this.setNote(note);
        return this;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public Matiere getMatiere() {
        return this.matiere;
    }

    public void setMatiere(Matiere matiere) {
        if (this.matiere != null) {
            this.matiere.setNote(null);
        }
        if (matiere != null) {
            matiere.setNote(this);
        }
        this.matiere = matiere;
    }

    public Note matiere(Matiere matiere) {
        this.setMatiere(matiere);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Note)) {
            return false;
        }
        return getId() != null && getId().equals(((Note) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Note{" +
            "id=" + getId() +
            ", note=" + getNote() +
            "}";
    }
}
