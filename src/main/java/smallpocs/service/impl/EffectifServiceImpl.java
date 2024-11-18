package smallpocs.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smallpocs.domain.Effectif;
import smallpocs.repository.EffectifRepository;
import smallpocs.service.EffectifService;
import smallpocs.service.dto.EffectifDTO;
import smallpocs.service.mapper.EffectifMapper;

/**
 * Service Implementation for managing {@link smallpocs.domain.Effectif}.
 */
@Service
@Transactional
public class EffectifServiceImpl implements EffectifService {

    private static final Logger LOG = LoggerFactory.getLogger(EffectifServiceImpl.class);

    private final EffectifRepository effectifRepository;

    private final EffectifMapper effectifMapper;

    public EffectifServiceImpl(EffectifRepository effectifRepository, EffectifMapper effectifMapper) {
        this.effectifRepository = effectifRepository;
        this.effectifMapper = effectifMapper;
    }

    @Override
    public EffectifDTO save(EffectifDTO effectifDTO) {
        LOG.debug("Request to save Effectif : {}", effectifDTO);
        Effectif effectif = effectifMapper.toEntity(effectifDTO);
        effectif = effectifRepository.save(effectif);
        return effectifMapper.toDto(effectif);
    }

    @Override
    public EffectifDTO update(EffectifDTO effectifDTO) {
        LOG.debug("Request to update Effectif : {}", effectifDTO);
        Effectif effectif = effectifMapper.toEntity(effectifDTO);
        effectif = effectifRepository.save(effectif);
        return effectifMapper.toDto(effectif);
    }

    @Override
    public Optional<EffectifDTO> partialUpdate(EffectifDTO effectifDTO) {
        LOG.debug("Request to partially update Effectif : {}", effectifDTO);

        return effectifRepository
            .findById(effectifDTO.getId())
            .map(existingEffectif -> {
                effectifMapper.partialUpdate(existingEffectif, effectifDTO);

                return existingEffectif;
            })
            .map(effectifRepository::save)
            .map(effectifMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EffectifDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Effectifs");
        return effectifRepository.findAll(pageable).map(effectifMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EffectifDTO> findOne(Long id) {
        LOG.debug("Request to get Effectif : {}", id);
        return effectifRepository.findById(id).map(effectifMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Effectif : {}", id);
        effectifRepository.deleteById(id);
    }
}
