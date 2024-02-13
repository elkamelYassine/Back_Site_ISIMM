package tn.isimm.manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Niveau.
 */
@Entity
@Table(name = "niveau")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Niveau implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "classe")
    private String classe;

    @Column(name = "tp")
    private String tp;

    @Column(name = "td")
    private String td;

    @JsonIgnoreProperties(value = { "niveau", "matiere" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Semestre semestre;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "niveau")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "matiere", "niveau" }, allowSetters = true)
    private Set<Seance> seances = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "niveau")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "fichierAdmins", "niveau", "clubs" }, allowSetters = true)
    private Set<Etudiant> etudiants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Niveau id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClasse() {
        return this.classe;
    }

    public Niveau classe(String classe) {
        this.setClasse(classe);
        return this;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getTp() {
        return this.tp;
    }

    public Niveau tp(String tp) {
        this.setTp(tp);
        return this;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    public String getTd() {
        return this.td;
    }

    public Niveau td(String td) {
        this.setTd(td);
        return this;
    }

    public void setTd(String td) {
        this.td = td;
    }

    public Semestre getSemestre() {
        return this.semestre;
    }

    public void setSemestre(Semestre semestre) {
        this.semestre = semestre;
    }

    public Niveau semestre(Semestre semestre) {
        this.setSemestre(semestre);
        return this;
    }

    public Set<Seance> getSeances() {
        return this.seances;
    }

    public void setSeances(Set<Seance> seances) {
        if (this.seances != null) {
            this.seances.forEach(i -> i.setNiveau(null));
        }
        if (seances != null) {
            seances.forEach(i -> i.setNiveau(this));
        }
        this.seances = seances;
    }

    public Niveau seances(Set<Seance> seances) {
        this.setSeances(seances);
        return this;
    }

    public Niveau addSeance(Seance seance) {
        this.seances.add(seance);
        seance.setNiveau(this);
        return this;
    }

    public Niveau removeSeance(Seance seance) {
        this.seances.remove(seance);
        seance.setNiveau(null);
        return this;
    }

    public Set<Etudiant> getEtudiants() {
        return this.etudiants;
    }

    public void setEtudiants(Set<Etudiant> etudiants) {
        if (this.etudiants != null) {
            this.etudiants.forEach(i -> i.setNiveau(null));
        }
        if (etudiants != null) {
            etudiants.forEach(i -> i.setNiveau(this));
        }
        this.etudiants = etudiants;
    }

    public Niveau etudiants(Set<Etudiant> etudiants) {
        this.setEtudiants(etudiants);
        return this;
    }

    public Niveau addEtudiant(Etudiant etudiant) {
        this.etudiants.add(etudiant);
        etudiant.setNiveau(this);
        return this;
    }

    public Niveau removeEtudiant(Etudiant etudiant) {
        this.etudiants.remove(etudiant);
        etudiant.setNiveau(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Niveau)) {
            return false;
        }
        return getId() != null && getId().equals(((Niveau) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Niveau{" +
            "id=" + getId() +
            ", classe='" + getClasse() + "'" +
            ", tp='" + getTp() + "'" +
            ", td='" + getTd() + "'" +
            "}";
    }
}
