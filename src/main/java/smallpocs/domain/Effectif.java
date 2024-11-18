package smallpocs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Effectif entity.
 */
@Entity
@Table(name = "effectif")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Effectif implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    /**
     * ;;;;;;;
     */
    @Column(name = "name")
    private String name;

    @Column(name = "cumul")
    private Long cumul;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "effectif")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "effectif" }, allowSetters = true)
    private Set<ChampEffectif> champs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Effectif id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Effectif name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCumul() {
        return this.cumul;
    }

    public Effectif cumul(Long cumul) {
        this.setCumul(cumul);
        return this;
    }

    public void setCumul(Long cumul) {
        this.cumul = cumul;
    }

    public Set<ChampEffectif> getChamps() {
        return this.champs;
    }

    public void setChamps(Set<ChampEffectif> champEffectifs) {
        if (this.champs != null) {
            this.champs.forEach(i -> i.setEffectif(null));
        }
        if (champEffectifs != null) {
            champEffectifs.forEach(i -> i.setEffectif(this));
        }
        this.champs = champEffectifs;
    }

    public Effectif champs(Set<ChampEffectif> champEffectifs) {
        this.setChamps(champEffectifs);
        return this;
    }

    public Effectif addChamps(ChampEffectif champEffectif) {
        this.champs.add(champEffectif);
        champEffectif.setEffectif(this);
        return this;
    }

    public Effectif removeChamps(ChampEffectif champEffectif) {
        this.champs.remove(champEffectif);
        champEffectif.setEffectif(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Effectif)) {
            return false;
        }
        return getId() != null && getId().equals(((Effectif) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Effectif{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", cumul=" + getCumul() +
            "}";
    }
}
