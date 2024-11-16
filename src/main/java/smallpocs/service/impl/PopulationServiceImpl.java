package smallpocs.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import smallpocs.domain.Population;
import smallpocs.repository.PopulationRepository;
import smallpocs.service.PopulationService;
import smallpocs.service.dto.PopulationDTO;
import smallpocs.service.mapper.PopulationMapper;

/**
 * Service Implementation for managing {@link smallpocs.domain.Population}.
 */
@Service
@Transactional
public class PopulationServiceImpl implements PopulationService {

    private static final Logger LOG = LoggerFactory.getLogger(PopulationServiceImpl.class);

    private final PopulationRepository populationRepository;

    private final PopulationMapper populationMapper;

    public PopulationServiceImpl(PopulationRepository populationRepository, PopulationMapper populationMapper) {
        this.populationRepository = populationRepository;
        this.populationMapper = populationMapper;
    }

    @Override
    public PopulationDTO save(PopulationDTO populationDTO) {
        LOG.debug("Request to save Population : {}", populationDTO);
        Population population = populationMapper.toEntity(populationDTO);
        population = populationRepository.save(population);
        return populationMapper.toDto(population);
    }

    @Override
    public PopulationDTO update(PopulationDTO populationDTO) {
        LOG.debug("Request to update Population : {}", populationDTO);
        Population population = populationMapper.toEntity(populationDTO);
        population = populationRepository.save(population);
        return populationMapper.toDto(population);
    }

    @Override
    public Optional<PopulationDTO> partialUpdate(PopulationDTO populationDTO) {
        LOG.debug("Request to partially update Population : {}", populationDTO);

        return populationRepository
            .findById(populationDTO.getId())
            .map(existingPopulation -> {
                populationMapper.partialUpdate(existingPopulation, populationDTO);

                return existingPopulation;
            })
            .map(populationRepository::save)
            .map(populationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PopulationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Populations");
        return populationRepository.findAll(pageable).map(populationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PopulationDTO> findOne(Long id) {
        LOG.debug("Request to get Population : {}", id);
        return populationRepository.findById(id).map(populationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Population : {}", id);
        populationRepository.deleteById(id);
    }
}
