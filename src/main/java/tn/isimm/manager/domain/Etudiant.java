package tn.isimm.manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Employee entity.
 */
@Schema(description = "The Employee entity.")
@Entity
@Table(name = "etudiant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Etudiant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * The firstname attribute.
     */
    @Schema(description = "The firstname attribute.")
    @Column(name = "prenom")
    private String prenom;

    @Column(name = "nom")
    private String nom;

    @Column(name = "email")
    private String email;

    @Column(name = "num_etudiant")
    private Long numEtudiant;

    @Column(name = "num_tel")
    private String numTel;

    @Column(name = "date_de_naissance")
    private LocalDate dateDeNaissance;

    @Lob
    @Column(name = "photo_de_profile")
    private byte[] photoDeProfile;

    @Column(name = "photo_de_profile_content_type")
    private String photoDeProfileContentType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "etudiant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "etudiant" }, allowSetters = true)
    private Set<FichierAdmin> fichierAdmins = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "semestre", "seances", "etudiants" }, allowSetters = true)
    private Niveau niveau;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_etudiant__club",
        joinColumns = @JoinColumn(name = "etudiant_id"),
        inverseJoinColumns = @JoinColumn(name = "club_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "etudiants" }, allowSetters = true)
    private Set<Club> clubs = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Etudiant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Etudiant prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return this.nom;
    }

    public Etudiant nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return this.email;
    }

    public Etudiant email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getNumEtudiant() {
        return this.numEtudiant;
    }

    public Etudiant numEtudiant(Long numEtudiant) {
        this.setNumEtudiant(numEtudiant);
        return this;
    }

    public void setNumEtudiant(Long numEtudiant) {
        this.numEtudiant = numEtudiant;
    }

    public String getNumTel() {
        return this.numTel;
    }

    public Etudiant numTel(String numTel) {
        this.setNumTel(numTel);
        return this;
    }

    public void setNumTel(String numTel) {
        this.numTel = numTel;
    }

    public LocalDate getDateDeNaissance() {
        return this.dateDeNaissance;
    }

    public Etudiant dateDeNaissance(LocalDate dateDeNaissance) {
        this.setDateDeNaissance(dateDeNaissance);
        return this;
    }

    public void setDateDeNaissance(LocalDate dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public byte[] getPhotoDeProfile() {
        return this.photoDeProfile;
    }

    public Etudiant photoDeProfile(byte[] photoDeProfile) {
        this.setPhotoDeProfile(photoDeProfile);
        return this;
    }

    public void setPhotoDeProfile(byte[] photoDeProfile) {
        this.photoDeProfile = photoDeProfile;
    }

    public String getPhotoDeProfileContentType() {
        return this.photoDeProfileContentType;
    }

    public Etudiant photoDeProfileContentType(String photoDeProfileContentType) {
        this.photoDeProfileContentType = photoDeProfileContentType;
        return this;
    }

    public void setPhotoDeProfileContentType(String photoDeProfileContentType) {
        this.photoDeProfileContentType = photoDeProfileContentType;
    }

    public Set<FichierAdmin> getFichierAdmins() {
        return this.fichierAdmins;
    }

    public void setFichierAdmins(Set<FichierAdmin> fichierAdmins) {
        if (this.fichierAdmins != null) {
            this.fichierAdmins.forEach(i -> i.setEtudiant(null));
        }
        if (fichierAdmins != null) {
            fichierAdmins.forEach(i -> i.setEtudiant(this));
        }
        this.fichierAdmins = fichierAdmins;
    }

    public Etudiant fichierAdmins(Set<FichierAdmin> fichierAdmins) {
        this.setFichierAdmins(fichierAdmins);
        return this;
    }

    public Etudiant addFichierAdmin(FichierAdmin fichierAdmin) {
        this.fichierAdmins.add(fichierAdmin);
        fichierAdmin.setEtudiant(this);
        return this;
    }

    public Etudiant removeFichierAdmin(FichierAdmin fichierAdmin) {
        this.fichierAdmins.remove(fichierAdmin);
        fichierAdmin.setEtudiant(null);
        return this;
    }

    public Niveau getNiveau() {
        return this.niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }

    public Etudiant niveau(Niveau niveau) {
        this.setNiveau(niveau);
        return this;
    }

    public Set<Club> getClubs() {
        return this.clubs;
    }

    public void setClubs(Set<Club> clubs) {
        this.clubs = clubs;
    }

    public Etudiant clubs(Set<Club> clubs) {
        this.setClubs(clubs);
        return this;
    }

    public Etudiant addClub(Club club) {
        this.clubs.add(club);
        return this;
    }

    public Etudiant removeClub(Club club) {
        this.clubs.remove(club);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Etudiant user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Etudiant)) {
            return false;
        }
        return getId() != null && getId().equals(((Etudiant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Etudiant{" +
            "id=" + getId() +
            ", prenom='" + getPrenom() + "'" +
            ", nom='" + getNom() + "'" +
            ", email='" + getEmail() + "'" +
            ", numEtudiant=" + getNumEtudiant() +
            ", numTel='" + getNumTel() + "'" +
            ", dateDeNaissance='" + getDateDeNaissance() + "'" +
            ", photoDeProfile='" + getPhotoDeProfile() + "'" +
            ", photoDeProfileContentType='" + getPhotoDeProfileContentType() + "'" +
            "}";
    }
}
