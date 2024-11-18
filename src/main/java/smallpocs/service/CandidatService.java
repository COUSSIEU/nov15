package smallpocs.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import smallpocs.service.dto.CandidatDTO;

/**
 * Service Interface for managing {@link smallpocs.domain.Candidat}.
 */
public interface CandidatService {
    /**
     * Save a candidat.
     *
     * @param candidatDTO the entity to save.
     * @return the persisted entity.
     */
    CandidatDTO save(CandidatDTO candidatDTO);

    /**
     * Updates a candidat.
     *
     * @param candidatDTO the entity to update.
     * @return the persisted entity.
     */
    CandidatDTO update(CandidatDTO candidatDTO);

    /**
     * Partially updates a candidat.
     *
     * @param candidatDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CandidatDTO> partialUpdate(CandidatDTO candidatDTO);

    /**
     * Get all the candidats.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CandidatDTO> findAll(Pageable pageable);

    /**
     * Get the "id" candidat.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CandidatDTO> findOne(Long id);

    /**
     * Delete the "id" candidat.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
