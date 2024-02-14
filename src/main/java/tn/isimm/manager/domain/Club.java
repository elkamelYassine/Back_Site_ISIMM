package tn.isimm.manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Club.
 */
@Entity
@Table(name = "club")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Club implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "page_fb")
    private String pageFB;

    @Column(name = "page_ig")
    private String pageIg;

    @Column(name = "email")
    private String email;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "clubs")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "fichierAdmins", "niveau", "clubs", "user" }, allowSetters = true)
    private Set<Etudiant> etudiants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Club id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Club nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPageFB() {
        return this.pageFB;
    }

    public Club pageFB(String pageFB) {
        this.setPageFB(pageFB);
        return this;
    }

    public void setPageFB(String pageFB) {
        this.pageFB = pageFB;
    }

    public String getPageIg() {
        return this.pageIg;
    }

    public Club pageIg(String pageIg) {
        this.setPageIg(pageIg);
        return this;
    }

    public void setPageIg(String pageIg) {
        this.pageIg = pageIg;
    }

    public String getEmail() {
        return this.email;
    }

    public Club email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Etudiant> getEtudiants() {
        return this.etudiants;
    }

    public void setEtudiants(Set<Etudiant> etudiants) {
        if (this.etudiants != null) {
            this.etudiants.forEach(i -> i.removeClub(this));
        }
        if (etudiants != null) {
            etudiants.forEach(i -> i.addClub(this));
        }
        this.etudiants = etudiants;
    }

    public Club etudiants(Set<Etudiant> etudiants) {
        this.setEtudiants(etudiants);
        return this;
    }

    public Club addEtudiant(Etudiant etudiant) {
        this.etudiants.add(etudiant);
        etudiant.getClubs().add(this);
        return this;
    }

    public Club removeEtudiant(Etudiant etudiant) {
        this.etudiants.remove(etudiant);
        etudiant.getClubs().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Club)) {
            return false;
        }
        return getId() != null && getId().equals(((Club) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Club{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", pageFB='" + getPageFB() + "'" +
            ", pageIg='" + getPageIg() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
