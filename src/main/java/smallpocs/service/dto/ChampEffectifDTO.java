package smallpocs.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link smallpocs.domain.ChampEffectif} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ChampEffectifDTO implements Serializable {

    private Long id;

    private String nom;

    private Long valeur;

    private EffectifDTO effectif;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Long getValeur() {
        return valeur;
    }

    public void setValeur(Long valeur) {
        this.valeur = valeur;
    }

    public EffectifDTO getEffectif() {
        return effectif;
    }

    public void setEffectif(EffectifDTO effectif) {
        this.effectif = effectif;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChampEffectifDTO)) {
            return false;
        }

        ChampEffectifDTO champEffectifDTO = (ChampEffectifDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, champEffectifDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ChampEffectifDTO{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", valeur=" + getValeur() +
            ", effectif=" + getEffectif() +
            "}";
    }
}
