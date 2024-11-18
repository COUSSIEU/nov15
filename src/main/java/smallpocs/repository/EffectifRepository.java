package smallpocs.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import smallpocs.domain.Effectif;

/**
 * Spring Data JPA repository for the Effectif entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EffectifRepository extends JpaRepository<Effectif, Long> {}
