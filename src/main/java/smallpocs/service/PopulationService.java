package smallpocs.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import smallpocs.service.dto.PopulationDTO;

/**
 * Service Interface for managing {@link smallpocs.domain.Population}.
 */
public interface PopulationService {
    /**
     * Save a population.
     *
     * @param populationDTO the entity to save.
     * @return the persisted entity.
     */
    PopulationDTO save(PopulationDTO populationDTO);

    /**
     * Updates a population.
     *
     * @param populationDTO the entity to update.
     * @return the persisted entity.
     */
    PopulationDTO update(PopulationDTO populationDTO);

    /**
     * Partially updates a population.
     *
     * @param populationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PopulationDTO> partialUpdate(PopulationDTO populationDTO);

    /**
     * Get all the populations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PopulationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" population.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PopulationDTO> findOne(Long id);

    /**
     * Delete the "id" population.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
