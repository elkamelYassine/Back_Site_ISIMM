package tn.isimm.manager.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Administrateur.
 */
@Entity
@Table(name = "administrateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Administrateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "nom")
    private String nom;

    @Column(name = "email")
    private String email;

    @Column(name = "matricule")
    private String matricule;

    @Column(name = "titre")
    private String titre;

    @Column(name = "num_tel")
    private String numTel;

    @Column(name = "date_de_naissance")
    private LocalDate dateDeNaissance;

    @Lob
    @Column(name = "photo_de_profile")
    private byte[] photoDeProfile;

    @Column(name = "photo_de_profile_content_type")
    private String photoDeProfileContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Administrateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Administrateur prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return this.nom;
    }

    public Administrateur nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return this.email;
    }

    public Administrateur email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatricule() {
        return this.matricule;
    }

    public Administrateur matricule(String matricule) {
        this.setMatricule(matricule);
        return this;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getTitre() {
        return this.titre;
    }

    public Administrateur titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getNumTel() {
        return this.numTel;
    }

    public Administrateur numTel(String numTel) {
        this.setNumTel(numTel);
        return this;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public LocalDate getDateDeNaissance() {
        return this.dateDeNaissance;
    }

    public Administrateur dateDeNaissance(LocalDate dateDeNaissance) {
        this.setDateDeNaissance(dateDeNaissance);
        return this;
    }

    public void setDateDeNaissance(LocalDate dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public byte[] getPhotoDeProfile() {
        return this.photoDeProfile;
    }

    public Administrateur photoDeProfile(byte[] photoDeProfile) {
        this.setPhotoDeProfile(photoDeProfile);
        return this;
    }

    public void setPhotoDeProfile(byte[] photoDeProfile) {
        this.photoDeProfile = photoDeProfile;
    }

    public String getPhotoDeProfileContentType() {
        return this.photoDeProfileContentType;
    }

    public Administrateur photoDeProfileContentType(String photoDeProfileContentType) {
        this.photoDeProfileContentType = photoDeProfileContentType;
        return this;
    }

    public void setPhotoDeProfileContentType(String photoDeProfileContentType) {
        this.photoDeProfileContentType = photoDeProfileContentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Administrateur)) {
            return false;
        }
        return getId() != null && getId().equals(((Administrateur) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Administrateur{" +
            "id=" + getId() +
            ", prenom='" + getPrenom() + "'" +
            ", nom='" + getNom() + "'" +
            ", email='" + getEmail() + "'" +
            ", matricule='" + getMatricule() + "'" +
            ", titre='" + getTitre() + "'" +
            ", numTel='" + getNumTel() + "'" +
            ", dateDeNaissance='" + getDateDeNaissance() + "'" +
            ", photoDeProfile='" + getPhotoDeProfile() + "'" +
            ", photoDeProfileContentType='" + getPhotoDeProfileContentType() + "'" +
            "}";
    }
}
