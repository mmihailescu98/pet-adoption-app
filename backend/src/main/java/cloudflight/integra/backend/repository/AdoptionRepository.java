package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.AdoptionEntry;
import cloudflight.integra.backend.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


@Repository
public interface AdoptionRepository extends JpaRepository<AdoptionEntry, Long> {
    List<AdoptionEntry> findByAdopterIsNull();
    Optional<AdoptionEntry> findByPetAndAdopterIsNull(Pet pet);

    @Query("SELECT COUNT(a) > 0 FROM AdoptionEntry a WHERE a.pet.id = :petId AND a.publisher.id = :userId AND a.adopter IS NULL")
    boolean existsByPetIdAndPublisherIdAndAdopterIsNull(@Param("petId") Integer petId, @Param("userId") Long userId);
}
