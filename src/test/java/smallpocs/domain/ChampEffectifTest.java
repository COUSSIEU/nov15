package smallpocs.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static smallpocs.domain.ChampEffectifTestSamples.*;
import static smallpocs.domain.EffectifTestSamples.*;

import org.junit.jupiter.api.Test;
import smallpocs.web.rest.TestUtil;

class ChampEffectifTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChampEffectif.class);
        ChampEffectif champEffectif1 = getChampEffectifSample1();
        ChampEffectif champEffectif2 = new ChampEffectif();
        assertThat(champEffectif1).isNotEqualTo(champEffectif2);

        champEffectif2.setId(champEffectif1.getId());
        assertThat(champEffectif1).isEqualTo(champEffectif2);

        champEffectif2 = getChampEffectifSample2();
        assertThat(champEffectif1).isNotEqualTo(champEffectif2);
    }

    @Test
    void effectifTest() {
        ChampEffectif champEffectif = getChampEffectifRandomSampleGenerator();
        Effectif effectifBack = getEffectifRandomSampleGenerator();

        champEffectif.setEffectif(effectifBack);
        assertThat(champEffectif.getEffectif()).isEqualTo(effectifBack);

        champEffectif.effectif(null);
        assertThat(champEffectif.getEffectif()).isNull();
    }
}
