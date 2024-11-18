package smallpocs.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smallpocs.domain.ChampEffectif;
import smallpocs.repository.ChampEffectifRepository;
import smallpocs.service.ChampEffectifService;
import smallpocs.service.dto.ChampEffectifDTO;
import smallpocs.service.mapper.ChampEffectifMapper;

/**
 * Service Implementation for managing {@link smallpocs.domain.ChampEffectif}.
 */
@Service
@Transactional
public class ChampEffectifServiceImpl implements ChampEffectifService {

    private static final Logger LOG = LoggerFactory.getLogger(ChampEffectifServiceImpl.class);

    private final ChampEffectifRepository champEffectifRepository;

    private final ChampEffectifMapper champEffectifMapper;

    public ChampEffectifServiceImpl(ChampEffectifRepository champEffectifRepository, ChampEffectifMapper champEffectifMapper) {
        this.champEffectifRepository = champEffectifRepository;
        this.champEffectifMapper = champEffectifMapper;
    }

    @Override
    public ChampEffectifDTO save(ChampEffectifDTO champEffectifDTO) {
        LOG.debug("Request to save ChampEffectif : {}", champEffectifDTO);
        ChampEffectif champEffectif = champEffectifMapper.toEntity(champEffectifDTO);
        champEffectif = champEffectifRepository.save(champEffectif);
        return champEffectifMapper.toDto(champEffectif);
    }

    @Override
    public ChampEffectifDTO update(ChampEffectifDTO champEffectifDTO) {
        LOG.debug("Request to update ChampEffectif : {}", champEffectifDTO);
        ChampEffectif champEffectif = champEffectifMapper.toEntity(champEffectifDTO);
        champEffectif = champEffectifRepository.save(champEffectif);
        return champEffectifMapper.toDto(champEffectif);
    }

    @Override
    public Optional<ChampEffectifDTO> partialUpdate(ChampEffectifDTO champEffectifDTO) {
        LOG.debug("Request to partially update ChampEffectif : {}", champEffectifDTO);

        return champEffectifRepository
            .findById(champEffectifDTO.getId())
            .map(existingChampEffectif -> {
                champEffectifMapper.partialUpdate(existingChampEffectif, champEffectifDTO);

                return existingChampEffectif;
            })
            .map(champEffectifRepository::save)
            .map(champEffectifMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChampEffectifDTO> findAll() {
        LOG.debug("Request to get all ChampEffectifs");
        return champEffectifRepository.findAll().stream().map(champEffectifMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChampEffectifDTO> findOne(Long id) {
        LOG.debug("Request to get ChampEffectif : {}", id);
        return champEffectifRepository.findById(id).map(champEffectifMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete ChampEffectif : {}", id);
        champEffectifRepository.deleteById(id);
    }
}
