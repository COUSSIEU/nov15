package smallpocs.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Materiel entity.
 */
@Entity
@Table(name = "materiel")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Materiel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * ;;;;;;;
     */
    @Column(name = "etat")
    private String etat;

    @Column(name = "release")
    private String release;

    @Column(name = "modele")
    private String modele;

    @Column(name = "sorte")
    private String sorte;

    @Column(name = "site")
    private String site;

    @Column(name = "region")
    private String region;

    @Column(name = "mission")
    private String mission;

    @Column(name = "entite")
    private String entite;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Materiel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtat() {
        return this.etat;
    }

    public Materiel etat(String etat) {
        this.setEtat(etat);
        return this;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getRelease() {
        return this.release;
    }

    public Materiel release(String release) {
        this.setRelease(release);
        return this;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getModele() {
        return this.modele;
    }

    public Materiel modele(String modele) {
        this.setModele(modele);
        return this;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getSorte() {
        return this.sorte;
    }

    public Materiel sorte(String sorte) {
        this.setSorte(sorte);
        return this;
    }

    public void setSorte(String sorte) {
        this.sorte = sorte;
    }

    public String getSite() {
        return this.site;
    }

    public Materiel site(String site) {
        this.setSite(site);
        return this;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getRegion() {
        return this.region;
    }

    public Materiel region(String region) {
        this.setRegion(region);
        return this;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getMission() {
        return this.mission;
    }

    public Materiel mission(String mission) {
        this.setMission(mission);
        return this;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getEntite() {
        return this.entite;
    }

    public Materiel entite(String entite) {
        this.setEntite(entite);
        return this;
    }

    public void setEntite(String entite) {
        this.entite = entite;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Materiel)) {
            return false;
        }
        return getId() != null && getId().equals(((Materiel) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Materiel{" +
            "id=" + getId() +
            ", etat='" + getEtat() + "'" +
            ", release='" + getRelease() + "'" +
            ", modele='" + getModele() + "'" +
            ", sorte='" + getSorte() + "'" +
            ", site='" + getSite() + "'" +
            ", region='" + getRegion() + "'" +
            ", mission='" + getMission() + "'" +
            ", entite='" + getEntite() + "'" +
            "}";
    }
}
