package smallpocs.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import smallpocs.service.dto.MaterielDTO;

/**
 * Service Interface for managing {@link smallpocs.domain.Materiel}.
 */
public interface MaterielService {
    /**
     * Save a materiel.
     *
     * @param materielDTO the entity to save.
     * @return the persisted entity.
     */
    MaterielDTO save(MaterielDTO materielDTO);

    /**
     * Updates a materiel.
     *
     * @param materielDTO the entity to update.
     * @return the persisted entity.
     */
    MaterielDTO update(MaterielDTO materielDTO);

    /**
     * Partially updates a materiel.
     *
     * @param materielDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MaterielDTO> partialUpdate(MaterielDTO materielDTO);

    /**
     * Get all the materiels.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MaterielDTO> findAll(Pageable pageable);

    /**
     * Get the "id" materiel.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MaterielDTO> findOne(Long id);

    /**
     * Delete the "id" materiel.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
