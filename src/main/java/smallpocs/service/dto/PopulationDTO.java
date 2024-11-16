package smallpocs.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link smallpocs.domain.Population} entity.
 */
@Schema(description = "The Population entity.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PopulationDTO implements Serializable {

    private Long id;

    private String tableName;

    private String attributNames;

    private String typeNames;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAttributNames() {
        return attributNames;
    }

    public void setAttributNames(String attributNames) {
        this.attributNames = attributNames;
    }

    public String getTypeNames() {
        return typeNames;
    }

    public void setTypeNames(String typeNames) {
        this.typeNames = typeNames;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PopulationDTO)) {
            return false;
        }

        PopulationDTO populationDTO = (PopulationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, populationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PopulationDTO{" +
            "id=" + getId() +
            ", tableName='" + getTableName() + "'" +
            ", attributNames='" + getAttributNames() + "'" +
            ", typeNames='" + getTypeNames() + "'" +
            "}";
    }
}
