package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.AdoptionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdoptionRepository extends JpaRepository<AdoptionRequest, Long> {
import cloudflight.integra.backend.model.AdoptionEntry;
import cloudflight.integra.backend.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AdoptionRepository extends JpaRepository<AdoptionEntry, Long> {
    List<AdoptionEntry> findByAdopterIsNull();
    Optional<AdoptionEntry> findByPetAndAdopterIsNull(Pet pet);
}
