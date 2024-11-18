package smallpocs.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import smallpocs.service.dto.EffectifDTO;

/**
 * Service Interface for managing {@link smallpocs.domain.Effectif}.
 */
public interface EffectifService {
    /**
     * Save a effectif.
     *
     * @param effectifDTO the entity to save.
     * @return the persisted entity.
     */
    EffectifDTO save(EffectifDTO effectifDTO);

    /**
     * Updates a effectif.
     *
     * @param effectifDTO the entity to update.
     * @return the persisted entity.
     */
    EffectifDTO update(EffectifDTO effectifDTO);

    /**
     * Partially updates a effectif.
     *
     * @param effectifDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EffectifDTO> partialUpdate(EffectifDTO effectifDTO);

    /**
     * Get all the effectifs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EffectifDTO> findAll(Pageable pageable);

    /**
     * Get the "id" effectif.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EffectifDTO> findOne(Long id);

    /**
     * Delete the "id" effectif.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
