package smallpocs.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import smallpocs.domain.ChampEffectif;

/**
 * Spring Data JPA repository for the ChampEffectif entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChampEffectifRepository extends JpaRepository<ChampEffectif, Long> {}
