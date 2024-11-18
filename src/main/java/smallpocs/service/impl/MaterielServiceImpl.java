package smallpocs.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smallpocs.domain.Materiel;
import smallpocs.repository.MaterielRepository;
import smallpocs.service.MaterielService;
import smallpocs.service.dto.MaterielDTO;
import smallpocs.service.mapper.MaterielMapper;

/**
 * Service Implementation for managing {@link smallpocs.domain.Materiel}.
 */
@Service
@Transactional
public class MaterielServiceImpl implements MaterielService {

    private static final Logger LOG = LoggerFactory.getLogger(MaterielServiceImpl.class);

    private final MaterielRepository materielRepository;

    private final MaterielMapper materielMapper;

    public MaterielServiceImpl(MaterielRepository materielRepository, MaterielMapper materielMapper) {
        this.materielRepository = materielRepository;
        this.materielMapper = materielMapper;
    }

    @Override
    public MaterielDTO save(MaterielDTO materielDTO) {
        LOG.debug("Request to save Materiel : {}", materielDTO);
        Materiel materiel = materielMapper.toEntity(materielDTO);
        materiel = materielRepository.save(materiel);
        return materielMapper.toDto(materiel);
    }

    @Override
    public MaterielDTO update(MaterielDTO materielDTO) {
        LOG.debug("Request to update Materiel : {}", materielDTO);
        Materiel materiel = materielMapper.toEntity(materielDTO);
        materiel = materielRepository.save(materiel);
        return materielMapper.toDto(materiel);
    }

    @Override
    public Optional<MaterielDTO> partialUpdate(MaterielDTO materielDTO) {
        LOG.debug("Request to partially update Materiel : {}", materielDTO);

        return materielRepository
            .findById(materielDTO.getId())
            .map(existingMateriel -> {
                materielMapper.partialUpdate(existingMateriel, materielDTO);

                return existingMateriel;
            })
            .map(materielRepository::save)
            .map(materielMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MaterielDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Materiels");
        return materielRepository.findAll(pageable).map(materielMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MaterielDTO> findOne(Long id) {
        LOG.debug("Request to get Materiel : {}", id);
        return materielRepository.findById(id).map(materielMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Materiel : {}", id);
        materielRepository.deleteById(id);
    }
}
