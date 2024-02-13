package tn.isimm.manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Semestre.
 */
@Entity
@Table(name = "semestre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Semestre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "annee_scolaire")
    private String anneeScolaire;

    @Column(name = "s")
    private Integer s;

    @JsonIgnoreProperties(value = { "semestre", "seances", "etudiants" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "semestre")
    private Niveau niveau;

    @JsonIgnoreProperties(value = { "semestre", "note", "cours", "seance", "professeurs" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "semestre")
    private Matiere matiere;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Semestre id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnneeScolaire() {
        return this.anneeScolaire;
    }

    public Semestre anneeScolaire(String anneeScolaire) {
        this.setAnneeScolaire(anneeScolaire);
        return this;
    }

    public void setAnneeScolaire(String anneeScolaire) {
        this.anneeScolaire = anneeScolaire;
    }

    public Integer getS() {
        return this.s;
    }

    public Semestre s(Integer s) {
        this.setS(s);
        return this;
    }

    public void setS(Integer s) {
        this.s = s;
    }

    public Niveau getNiveau() {
        return this.niveau;
    }

    public void setNiveau(Niveau niveau) {
        if (this.niveau != null) {
            this.niveau.setSemestre(null);
        }
        if (niveau != null) {
            niveau.setSemestre(this);
        }
        this.niveau = niveau;
    }

    public Semestre niveau(Niveau niveau) {
        this.setNiveau(niveau);
        return this;
    }

    public Matiere getMatiere() {
        return this.matiere;
    }

    public void setMatiere(Matiere matiere) {
        if (this.matiere != null) {
            this.matiere.setSemestre(null);
        }
        if (matiere != null) {
            matiere.setSemestre(this);
        }
        this.matiere = matiere;
    }

    public Semestre matiere(Matiere matiere) {
        this.setMatiere(matiere);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Semestre)) {
            return false;
        }
        return getId() != null && getId().equals(((Semestre) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Semestre{" +
            "id=" + getId() +
            ", anneeScolaire='" + getAnneeScolaire() + "'" +
            ", s=" + getS() +
            "}";
    }
}
