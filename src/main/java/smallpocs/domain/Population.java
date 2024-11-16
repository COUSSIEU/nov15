package smallpocs.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Population entity.
 */
@Entity
@Table(name = "population")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Population implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "attribut_names")
    private String attributNames;

    @Column(name = "type_names")
    private String typeNames;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Population id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTableName() {
        return this.tableName;
    }

    public Population tableName(String tableName) {
        this.setTableName(tableName);
        return this;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getAttributNames() {
        return this.attributNames;
    }

    public Population attributNames(String attributNames) {
        this.setAttributNames(attributNames);
        return this;
    }

    public void setAttributNames(String attributNames) {
        this.attributNames = attributNames;
    }

    public String getTypeNames() {
        return this.typeNames;
    }

    public Population typeNames(String typeNames) {
        this.setTypeNames(typeNames);
        return this;
    }

    public void setTypeNames(String typeNames) {
        this.typeNames = typeNames;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Population)) {
            return false;
        }
        return getId() != null && getId().equals(((Population) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Population{" +
            "id=" + getId() +
            ", tableName='" + getTableName() + "'" +
            ", attributNames='" + getAttributNames() + "'" +
            ", typeNames='" + getTypeNames() + "'" +
            "}";
    }
}
