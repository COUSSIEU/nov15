package smallpocs.service.mapper;

import org.mapstruct.*;
import smallpocs.domain.Effectif;
import smallpocs.service.dto.EffectifDTO;

/**
 * Mapper for the entity {@link Effectif} and its DTO {@link EffectifDTO}.
 */
@Mapper(componentModel = "spring")
public interface EffectifMapper extends EntityMapper<EffectifDTO, Effectif> {}
