package smallpocs.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link smallpocs.domain.Effectif} entity.
 */
@Schema(description = "The Effectif entity.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EffectifDTO implements Serializable {

    private Long id;

    @Schema(description = ";;;;;;;")
    private String name;

    private Long cumul;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCumul() {
        return cumul;
    }

    public void setCumul(Long cumul) {
        this.cumul = cumul;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EffectifDTO)) {
            return false;
        }

        EffectifDTO effectifDTO = (EffectifDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, effectifDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EffectifDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", cumul=" + getCumul() +
            "}";
    }
}
