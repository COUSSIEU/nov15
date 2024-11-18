package smallpocs.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * The Candidat entity.
 */
@Entity
@Table(name = "candidat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Candidat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "age")
    private Long age;

    @Column(name = "springboot")
    private Long springboot;

    @Column(name = "angular")
    private Long angular;

    @Column(name = "html")
    private Long html;

    @Column(name = "css")
    private Long css;

    @Column(name = "transport")
    private Long transport;

    @Column(name = "sport")
    private String sport;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Candidat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return this.nom;
    }

    public Candidat nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Long getAge() {
        return this.age;
    }

    public Candidat age(Long age) {
        this.setAge(age);
        return this;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Long getSpringboot() {
        return this.springboot;
    }

    public Candidat springboot(Long springboot) {
        this.setSpringboot(springboot);
        return this;
    }

    public void setSpringboot(Long springboot) {
        this.springboot = springboot;
    }

    public Long getAngular() {
        return this.angular;
    }

    public Candidat angular(Long angular) {
        this.setAngular(angular);
        return this;
    }

    public void setAngular(Long angular) {
        this.angular = angular;
    }

    public Long getHtml() {
        return this.html;
    }

    public Candidat html(Long html) {
        this.setHtml(html);
        return this;
    }

    public void setHtml(Long html) {
        this.html = html;
    }

    public Long getCss() {
        return this.css;
    }

    public Candidat css(Long css) {
        this.setCss(css);
        return this;
    }

    public void setCss(Long css) {
        this.css = css;
    }

    public Long getTransport() {
        return this.transport;
    }

    public Candidat transport(Long transport) {
        this.setTransport(transport);
        return this;
    }

    public void setTransport(Long transport) {
        this.transport = transport;
    }

    public String getSport() {
        return this.sport;
    }

    public Candidat sport(String sport) {
        this.setSport(sport);
        return this;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Candidat)) {
            return false;
        }
        return getId() != null && getId().equals(((Candidat) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Candidat{" +
            "id=" + getId() +
            ", nom='" + getNom() + "'" +
            ", age=" + getAge() +
            ", springboot=" + getSpringboot() +
            ", angular=" + getAngular() +
            ", html=" + getHtml() +
            ", css=" + getCss() +
            ", transport=" + getTransport() +
            ", sport='" + getSport() + "'" +
            "}";
    }
}
