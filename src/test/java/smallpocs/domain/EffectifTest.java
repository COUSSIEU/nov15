package smallpocs.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static smallpocs.domain.ChampEffectifTestSamples.*;
import static smallpocs.domain.EffectifTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import smallpocs.web.rest.TestUtil;

class EffectifTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Effectif.class);
        Effectif effectif1 = getEffectifSample1();
        Effectif effectif2 = new Effectif();
        assertThat(effectif1).isNotEqualTo(effectif2);

        effectif2.setId(effectif1.getId());
        assertThat(effectif1).isEqualTo(effectif2);

        effectif2 = getEffectifSample2();
        assertThat(effectif1).isNotEqualTo(effectif2);
    }

    @Test
    void champsTest() {
        Effectif effectif = getEffectifRandomSampleGenerator();
        ChampEffectif champEffectifBack = getChampEffectifRandomSampleGenerator();

        effectif.addChamps(champEffectifBack);
        assertThat(effectif.getChamps()).containsOnly(champEffectifBack);
        assertThat(champEffectifBack.getEffectif()).isEqualTo(effectif);

        effectif.removeChamps(champEffectifBack);
        assertThat(effectif.getChamps()).doesNotContain(champEffectifBack);
        assertThat(champEffectifBack.getEffectif()).isNull();

        effectif.champs(new HashSet<>(Set.of(champEffectifBack)));
        assertThat(effectif.getChamps()).containsOnly(champEffectifBack);
        assertThat(champEffectifBack.getEffectif()).isEqualTo(effectif);

        effectif.setChamps(new HashSet<>());
        assertThat(effectif.getChamps()).doesNotContain(champEffectifBack);
        assertThat(champEffectifBack.getEffectif()).isNull();
    }
}
