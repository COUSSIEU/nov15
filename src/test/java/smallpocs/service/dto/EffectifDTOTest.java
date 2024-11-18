package smallpocs.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import smallpocs.web.rest.TestUtil;

class EffectifDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EffectifDTO.class);
        EffectifDTO effectifDTO1 = new EffectifDTO();
        effectifDTO1.setId(1L);
        EffectifDTO effectifDTO2 = new EffectifDTO();
        assertThat(effectifDTO1).isNotEqualTo(effectifDTO2);
        effectifDTO2.setId(effectifDTO1.getId());
        assertThat(effectifDTO1).isEqualTo(effectifDTO2);
        effectifDTO2.setId(2L);
        assertThat(effectifDTO1).isNotEqualTo(effectifDTO2);
        effectifDTO1.setId(null);
        assertThat(effectifDTO1).isNotEqualTo(effectifDTO2);
    }
}
