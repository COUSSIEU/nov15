package smallpocs.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link smallpocs.domain.Materiel} entity.
 */
@Schema(description = "The Materiel entity.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MaterielDTO implements Serializable {

    private Long id;

    @Schema(description = ";;;;;;;")
    private String etat;

    private String release;

    private String modele;

    private String sorte;

    private String site;

    private String region;

    private String mission;

    private String entite;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getSorte() {
        return sorte;
    }

    public void setSorte(String sorte) {
        this.sorte = sorte;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getEntite() {
        return entite;
    }

    public void setEntite(String entite) {
        this.entite = entite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MaterielDTO)) {
            return false;
        }

        MaterielDTO materielDTO = (MaterielDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, materielDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MaterielDTO{" +
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
