package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.AdoptionEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AdoptionRepository extends JpaRepository<AdoptionEntry, Integer> {}
