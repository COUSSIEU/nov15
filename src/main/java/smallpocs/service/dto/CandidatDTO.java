package smallpocs.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link smallpocs.domain.Candidat} entity.
 */
@Schema(description = "The Candidat entity.")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CandidatDTO implements Serializable {

    private Long id;

    private String nom;

    private Long age;

    private Long springboot;

    private Long angular;

    private Long html;

    private Long css;

    private Long transport;

    private String sport;

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

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }

    public Long getSpringboot() {
        return springboot;
    }

    public void setSpringboot(Long springboot) {
        this.springboot = springboot;
    }

    public Long getAngular() {
        return angular;
    }

    public void setAngular(Long angular) {
        this.angular = angular;
    }

    public Long getHtml() {
        return html;
    }

    public void setHtml(Long html) {
        this.html = html;
    }

    public Long getCss() {
        return css;
    }

    public void setCss(Long css) {
        this.css = css;
    }

    public Long getTransport() {
        return transport;
    }

    public void setTransport(Long transport) {
        this.transport = transport;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CandidatDTO)) {
            return false;
        }

        CandidatDTO candidatDTO = (CandidatDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, candidatDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CandidatDTO{" +
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
