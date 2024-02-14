package tn.isimm.manager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import tn.isimm.manager.domain.enumeration.TypeFichierAdmin;

/**
 * A FichierAdmin.
 */
@Entity
@Table(name = "fichier_admin")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FichierAdmin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "titre")
    private String titre;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private TypeFichierAdmin type;

    @Lob
    @Column(name = "file")
    private byte[] file;

    @Column(name = "file_content_type")
    private String fileContentType;

    @Column(name = "demande_valide")
    private Boolean demandeValide;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "fichierAdmins", "niveau", "clubs", "user" }, allowSetters = true)
    private Etudiant etudiant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FichierAdmin id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return this.titre;
    }

    public FichierAdmin titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public TypeFichierAdmin getType() {
        return this.type;
    }

    public FichierAdmin type(TypeFichierAdmin type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeFichierAdmin type) {
        this.type = type;
    }

    public byte[] getFile() {
        return this.file;
    }

    public FichierAdmin file(byte[] file) {
        this.setFile(file);
        return this;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileContentType() {
        return this.fileContentType;
    }

    public FichierAdmin fileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
        return this;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }

    public Boolean getDemandeValide() {
        return this.demandeValide;
    }

    public FichierAdmin demandeValide(Boolean demandeValide) {
        this.setDemandeValide(demandeValide);
        return this;
    }

    public void setDemandeValide(Boolean demandeValide) {
        this.demandeValide = demandeValide;
    }

    public Etudiant getEtudiant() {
        return this.etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public FichierAdmin etudiant(Etudiant etudiant) {
        this.setEtudiant(etudiant);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FichierAdmin)) {
            return false;
        }
        return getId() != null && getId().equals(((FichierAdmin) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FichierAdmin{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", type='" + getType() + "'" +
            ", file='" + getFile() + "'" +
            ", fileContentType='" + getFileContentType() + "'" +
            ", demandeValide='" + getDemandeValide() + "'" +
            "}";
    }
}
