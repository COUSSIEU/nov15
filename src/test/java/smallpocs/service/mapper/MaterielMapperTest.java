package smallpocs.service.mapper;

import static smallpocs.domain.MaterielAsserts.*;
import static smallpocs.domain.MaterielTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MaterielMapperTest {

    private MaterielMapper materielMapper;

    @BeforeEach
    void setUp() {
        materielMapper = new MaterielMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getMaterielSample1();
        var actual = materielMapper.toEntity(materielMapper.toDto(expected));
        assertMaterielAllPropertiesEquals(expected, actual);
    }
}
