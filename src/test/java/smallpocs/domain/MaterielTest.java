package smallpocs.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static smallpocs.domain.MaterielTestSamples.*;

import org.junit.jupiter.api.Test;
import smallpocs.web.rest.TestUtil;

class MaterielTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Materiel.class);
        Materiel materiel1 = getMaterielSample1();
        Materiel materiel2 = new Materiel();
        assertThat(materiel1).isNotEqualTo(materiel2);

        materiel2.setId(materiel1.getId());
        assertThat(materiel1).isEqualTo(materiel2);

        materiel2 = getMaterielSample2();
        assertThat(materiel1).isNotEqualTo(materiel2);
    }
}
