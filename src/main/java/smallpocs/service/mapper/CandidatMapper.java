package smallpocs.service.mapper;

import org.mapstruct.*;
import smallpocs.domain.Candidat;
import smallpocs.service.dto.CandidatDTO;

/**
 * Mapper for the entity {@link Candidat} and its DTO {@link CandidatDTO}.
 */
@Mapper(componentModel = "spring")
public interface CandidatMapper extends EntityMapper<CandidatDTO, Candidat> {}
