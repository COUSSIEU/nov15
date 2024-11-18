package smallpocs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import smallpocs.web.rest.TestUtil;

class ChampEffectifDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChampEffectifDTO.class);
        ChampEffectifDTO champEffectifDTO1 = new ChampEffectifDTO();
        champEffectifDTO1.setId(1L);
        ChampEffectifDTO champEffectifDTO2 = new ChampEffectifDTO();
        assertThat(champEffectifDTO1).isNotEqualTo(champEffectifDTO2);
        champEffectifDTO2.setId(champEffectifDTO1.getId());
        assertThat(champEffectifDTO1).isEqualTo(champEffectifDTO2);
        champEffectifDTO2.setId(2L);
        assertThat(champEffectifDTO1).isNotEqualTo(champEffectifDTO2);
        champEffectifDTO1.setId(null);
        assertThat(champEffectifDTO1).isNotEqualTo(champEffectifDTO2);
    }
}
