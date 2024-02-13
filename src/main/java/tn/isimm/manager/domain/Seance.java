package tn.isimm.manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import tn.isimm.manager.domain.enumeration.Jours;
import tn.isimm.manager.domain.enumeration.Salle;

/**
 * A Seance.
 */
@Entity
@Table(name = "seance")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Seance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "jour")
    private Jours jour;

    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "num_seance")
    private Integer numSeance;

    @Enumerated(EnumType.STRING)
    @Column(name = "salle")
    private Salle salle;

    @JsonIgnoreProperties(value = { "semestre", "note", "cours", "seance", "professeurs" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Matiere matiere;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "semestre", "seances", "etudiants" }, allowSetters = true)
    private Niveau niveau;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Seance id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Jours getJour() {
        return this.jour;
    }

    public Seance jour(Jours jour) {
        this.setJour(jour);
        return this;
    }

    public void setJour(Jours jour) {
        this.jour = jour;
    }

    public Integer getNumSeance() {
        return this.numSeance;
    }

    public Seance numSeance(Integer numSeance) {
        this.setNumSeance(numSeance);
        return this;
    }

    public void setNumSeance(Integer numSeance) {
        this.numSeance = numSeance;
    }

    public Salle getSalle() {
        return this.salle;
    }

    public Seance salle(Salle salle) {
        this.setSalle(salle);
        return this;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    public Matiere getMatiere() {
        return this.matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public Seance matiere(Matiere matiere) {
        this.setMatiere(matiere);
        return this;
    }

    public Niveau getNiveau() {
        return this.niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public Seance niveau(Niveau niveau) {
        this.setNiveau(niveau);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Seance)) {
            return false;
        }
        return getId() != null && getId().equals(((Seance) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Seance{" +
            "id=" + getId() +
            ", jour='" + getJour() + "'" +
            ", numSeance=" + getNumSeance() +
            ", salle='" + getSalle() + "'" +
            "}";
    }
}
