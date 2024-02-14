package tn.isimm.manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import tn.isimm.manager.domain.enumeration.Departement;

/**
 * A Professeur.
 */
@Entity
@Table(name = "professeur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Professeur implements Serializable {

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

    @Enumerated(EnumType.STRING)
    @Column(name = "departement")
    private Departement departement;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_professeur__matiere",
        joinColumns = @JoinColumn(name = "professeur_id"),
        inverseJoinColumns = @JoinColumn(name = "matiere_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "semestre", "note", "cours", "seance", "professeurs" }, allowSetters = true)
    private Set<Matiere> matieres = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Professeur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Professeur prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return this.nom;
    }

    public Professeur nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return this.email;
    }

    public Professeur email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMatricule() {
        return this.matricule;
    }

    public Professeur matricule(String matricule) {
        this.setMatricule(matricule);
        return this;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public Departement getDepartement() {
        return this.departement;
    }

    public Professeur departement(Departement departement) {
        this.setDepartement(departement);
        return this;
    }

    public void setDepartement(Departement departement) {
        this.departement = departement;
    }

    public String getTitre() {
        return this.titre;
    }

    public Professeur titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getNumTel() {
        return this.numTel;
    }

    public Professeur numTel(String numTel) {
        this.setNumTel(numTel);
        return this;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public LocalDate getDateDeNaissance() {
        return this.dateDeNaissance;
    }

    public Professeur dateDeNaissance(LocalDate dateDeNaissance) {
        this.setDateDeNaissance(dateDeNaissance);
        return this;
    }

    public void setDateDeNaissance(LocalDate dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public byte[] getPhotoDeProfile() {
        return this.photoDeProfile;
    }

    public Professeur photoDeProfile(byte[] photoDeProfile) {
        this.setPhotoDeProfile(photoDeProfile);
        return this;
    }

    public void setPhotoDeProfile(byte[] photoDeProfile) {
        this.photoDeProfile = photoDeProfile;
    }

    public String getPhotoDeProfileContentType() {
        return this.photoDeProfileContentType;
    }

    public Professeur photoDeProfileContentType(String photoDeProfileContentType) {
        this.photoDeProfileContentType = photoDeProfileContentType;
        return this;
    }

    public void setPhotoDeProfileContentType(String photoDeProfileContentType) {
        this.photoDeProfileContentType = photoDeProfileContentType;
    }

    public Set<Matiere> getMatieres() {
        return this.matieres;
    }

    public void setMatieres(Set<Matiere> matieres) {
        this.matieres = matieres;
    }

    public Professeur matieres(Set<Matiere> matieres) {
        this.setMatieres(matieres);
        return this;
    }

    public Professeur addMatiere(Matiere matiere) {
        this.matieres.add(matiere);
        return this;
    }

    public Professeur removeMatiere(Matiere matiere) {
        this.matieres.remove(matiere);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Professeur user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Professeur)) {
            return false;
        }
        return getId() != null && getId().equals(((Professeur) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Professeur{" +
            "id=" + getId() +
            ", prenom='" + getPrenom() + "'" +
            ", nom='" + getNom() + "'" +
            ", email='" + getEmail() + "'" +
            ", matricule='" + getMatricule() + "'" +
            ", departement='" + getDepartement() + "'" +
            ", titre='" + getTitre() + "'" +
            ", numTel='" + getNumTel() + "'" +
            ", dateDeNaissance='" + getDateDeNaissance() + "'" +
            ", photoDeProfile='" + getPhotoDeProfile() + "'" +
            ", photoDeProfileContentType='" + getPhotoDeProfileContentType() + "'" +
            "}";
    }
}
