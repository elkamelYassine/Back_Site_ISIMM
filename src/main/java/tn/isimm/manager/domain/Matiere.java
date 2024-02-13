package tn.isimm.manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Matiere.
 */
@Entity
@Table(name = "matiere")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Matiere implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_matiere")
    private String nomMatiere;

    @JsonIgnoreProperties(value = { "niveau", "matiere" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Semestre semestre;

    @JsonIgnoreProperties(value = { "matiere" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Note note;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "matiere")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "matiere" }, allowSetters = true)
    private Set<Cours> cours = new HashSet<>();

    @JsonIgnoreProperties(value = { "matiere", "niveau" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "matiere")
    private Seance seance;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "matieres")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "matieres" }, allowSetters = true)
    private Set<Professeur> professeurs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Matiere id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomMatiere() {
        return this.nomMatiere;
    }

    public Matiere nomMatiere(String nomMatiere) {
        this.setNomMatiere(nomMatiere);
        return this;
    }

    public void setNomMatiere(String nomMatiere) {
        this.nomMatiere = nomMatiere;
    }

    public Semestre getSemestre() {
        return this.semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }

    public Matiere semestre(Semestre semestre) {
        this.setSemestre(semestre);
        return this;
    }

    public Note getNote() {
        return this.note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public Matiere note(Note note) {
        this.setNote(note);
        return this;
    }

    public Set<Cours> getCours() {
        return this.cours;
    }

    public void setCours(Set<Cours> cours) {
        if (this.cours != null) {
            this.cours.forEach(i -> i.setMatiere(null));
        }
        if (cours != null) {
            cours.forEach(i -> i.setMatiere(this));
        }
        this.cours = cours;
    }

    public Matiere cours(Set<Cours> cours) {
        this.setCours(cours);
        return this;
    }

    public Matiere addCours(Cours cours) {
        this.cours.add(cours);
        cours.setMatiere(this);
        return this;
    }

    public Matiere removeCours(Cours cours) {
        this.cours.remove(cours);
        cours.setMatiere(null);
        return this;
    }

    public Seance getSeance() {
        return this.seance;
    }

    public void setSeance(Seance seance) {
        if (this.seance != null) {
            this.seance.setMatiere(null);
        }
        if (seance != null) {
            seance.setMatiere(this);
        }
        this.seance = seance;
    }

    public Matiere seance(Seance seance) {
        this.setSeance(seance);
        return this;
    }

    public Set<Professeur> getProfesseurs() {
        return this.professeurs;
    }

    public void setProfesseurs(Set<Professeur> professeurs) {
        if (this.professeurs != null) {
            this.professeurs.forEach(i -> i.removeMatiere(this));
        }
        if (professeurs != null) {
            professeurs.forEach(i -> i.addMatiere(this));
        }
        this.professeurs = professeurs;
    }

    public Matiere professeurs(Set<Professeur> professeurs) {
        this.setProfesseurs(professeurs);
        return this;
    }

    public Matiere addProfesseur(Professeur professeur) {
        this.professeurs.add(professeur);
        professeur.getMatieres().add(this);
        return this;
    }

    public Matiere removeProfesseur(Professeur professeur) {
        this.professeurs.remove(professeur);
        professeur.getMatieres().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Matiere)) {
            return false;
        }
        return getId() != null && getId().equals(((Matiere) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Matiere{" +
            "id=" + getId() +
            ", nomMatiere='" + getNomMatiere() + "'" +
            "}";
    }
}
