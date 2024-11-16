package smallpocs.service.mapper;

import org.mapstruct.*;
import smallpocs.domain.Population;
import smallpocs.service.dto.PopulationDTO;

/**
 * Mapper for the entity {@link Population} and its DTO {@link PopulationDTO}.
 */
@Mapper(componentModel = "spring")
public interface PopulationMapper extends EntityMapper<PopulationDTO, Population> {}
