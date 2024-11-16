package smallpocs.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static smallpocs.domain.PopulationTestSamples.*;

import org.junit.jupiter.api.Test;
import smallpocs.web.rest.TestUtil;

class PopulationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Population.class);
        Population population1 = getPopulationSample1();
        Population population2 = new Population();
        assertThat(population1).isNotEqualTo(population2);

        population2.setId(population1.getId());
        assertThat(population1).isEqualTo(population2);

        population2 = getPopulationSample2();
        assertThat(population1).isNotEqualTo(population2);
    }
}
