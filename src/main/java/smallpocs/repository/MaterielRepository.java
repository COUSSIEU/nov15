package smallpocs.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import smallpocs.domain.Materiel;

/**
 * Spring Data JPA repository for the Materiel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaterielRepository extends JpaRepository<Materiel, Long> {}
