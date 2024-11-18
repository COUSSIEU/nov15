package smallpocs.service;

import java.util.List;
import java.util.Optional;
import smallpocs.service.dto.ChampEffectifDTO;

/**
 * Service Interface for managing {@link smallpocs.domain.ChampEffectif}.
 */
public interface ChampEffectifService {
    /**
     * Save a champEffectif.
     *
     * @param champEffectifDTO the entity to save.
     * @return the persisted entity.
     */
    ChampEffectifDTO save(ChampEffectifDTO champEffectifDTO);

    /**
     * Updates a champEffectif.
     *
     * @param champEffectifDTO the entity to update.
     * @return the persisted entity.
     */
    ChampEffectifDTO update(ChampEffectifDTO champEffectifDTO);

    /**
     * Partially updates a champEffectif.
     *
     * @param champEffectifDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ChampEffectifDTO> partialUpdate(ChampEffectifDTO champEffectifDTO);

    /**
     * Get all the champEffectifs.
     *
     * @return the list of entities.
     */
    List<ChampEffectifDTO> findAll();

    /**
     * Get the "id" champEffectif.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChampEffectifDTO> findOne(Long id);

    /**
     * Delete the "id" champEffectif.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
