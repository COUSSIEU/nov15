package smallpocs.service.mapper;

import org.mapstruct.*;
import smallpocs.domain.Materiel;
import smallpocs.service.dto.MaterielDTO;

/**
 * Mapper for the entity {@link Materiel} and its DTO {@link MaterielDTO}.
 */
@Mapper(componentModel = "spring")
public interface MaterielMapper extends EntityMapper<MaterielDTO, Materiel> {}
