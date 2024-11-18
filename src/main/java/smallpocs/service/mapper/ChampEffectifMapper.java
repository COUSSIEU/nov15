package smallpocs.service.mapper;

import org.mapstruct.*;
import smallpocs.domain.ChampEffectif;
import smallpocs.domain.Effectif;
import smallpocs.service.dto.ChampEffectifDTO;
import smallpocs.service.dto.EffectifDTO;

/**
 * Mapper for the entity {@link ChampEffectif} and its DTO {@link ChampEffectifDTO}.
 */
@Mapper(componentModel = "spring")
public interface ChampEffectifMapper extends EntityMapper<ChampEffectifDTO, ChampEffectif> {
    @Mapping(target = "effectif", source = "effectif", qualifiedByName = "effectifId")
    ChampEffectifDTO toDto(ChampEffectif s);

    @Named("effectifId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EffectifDTO toDtoEffectifId(Effectif effectif);
}
