package smallpocs.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import smallpocs.domain.Population;

/**
 * Spring Data JPA repository for the Population entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PopulationRepository extends JpaRepository<Population, Long> {}
