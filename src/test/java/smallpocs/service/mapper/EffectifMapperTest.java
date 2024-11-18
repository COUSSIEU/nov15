package smallpocs.service.mapper;

import static smallpocs.domain.EffectifAsserts.*;
import static smallpocs.domain.EffectifTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EffectifMapperTest {

    private EffectifMapper effectifMapper;

    @BeforeEach
    void setUp() {
        effectifMapper = new EffectifMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEffectifSample1();
        var actual = effectifMapper.toEntity(effectifMapper.toDto(expected));
        assertEffectifAllPropertiesEquals(expected, actual);
    }
}
