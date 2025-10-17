package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.AdoptionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdoptionRepository extends JpaRepository<AdoptionRequest, Long> {
}
