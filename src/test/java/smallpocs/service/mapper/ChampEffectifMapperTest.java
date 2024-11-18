package smallpocs.service.mapper;

import static smallpocs.domain.ChampEffectifAsserts.*;
import static smallpocs.domain.ChampEffectifTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ChampEffectifMapperTest {

    private ChampEffectifMapper champEffectifMapper;

    @BeforeEach
    void setUp() {
        champEffectifMapper = new ChampEffectifMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getChampEffectifSample1();
        var actual = champEffectifMapper.toEntity(champEffectifMapper.toDto(expected));
        assertChampEffectifAllPropertiesEquals(expected, actual);
    }
}
