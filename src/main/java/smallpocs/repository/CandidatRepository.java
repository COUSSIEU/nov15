package smallpocs.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import smallpocs.domain.Candidat;

/**
 * Spring Data JPA repository for the Candidat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CandidatRepository extends JpaRepository<Candidat, Long> {}
