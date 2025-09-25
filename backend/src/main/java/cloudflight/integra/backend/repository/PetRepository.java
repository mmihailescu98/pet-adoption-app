package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.dto.AdoptionStatsDTO;
import cloudflight.integra.backend.dto.PercentageStatsDTO;
import cloudflight.integra.backend.model.Pet;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Integer> {

    List<Pet> findPetsByName(String name);

    @Query("select p from Pet p where (:species is null or :species = '' or lower(p.species) like lower(concat('%', :species, '%'))) and (:breed is null or :breed = '' or lower(p.breed) like lower(concat('%', :breed, '%')))")
    List<Pet> filterPets(@Param("species") String species, @Param("breed") String breed, Sort sort);

    @Query("SELECT new cloudflight.integra.backend.dto.AdoptionStatsDTO(cloudflight.integra.backend.model.StatsType.SPECIES, p.species, COUNT(p)) " +
            "FROM Pet p WHERE p.adoptionStatus = 'ADOPTED' GROUP BY p.species ORDER BY COUNT(p) DESC")
    List<AdoptionStatsDTO> getAdoptedSpeciesStats();

    @Query("SELECT new cloudflight.integra.backend.dto.AdoptionStatsDTO(cloudflight.integra.backend.model.StatsType.BREED, p.breed, COUNT(p)) " +
            "FROM Pet p WHERE p.adoptionStatus = 'ADOPTED' GROUP BY p.breed ORDER BY COUNT(p) DESC")
    List<AdoptionStatsDTO> getAdoptedBreedStats();

    @Query("SELECT new cloudflight.integra.backend.dto.PercentageStatsDTO(cloudflight.integra.backend.model.StatsType.SPECIES, p.species, (COUNT(p) * 100.0 / (SELECT COUNT(p2) FROM Pet p2))) " +
            "FROM Pet p GROUP BY p.species ORDER BY COUNT(p) DESC")
    List<PercentageStatsDTO> getSpeciesPercentageStats();

    @Query("SELECT new cloudflight.integra.backend.dto.PercentageStatsDTO(cloudflight.integra.backend.model.StatsType.BREED, p.breed, (COUNT(p) * 100.0 / (SELECT COUNT(p2) FROM Pet p2))) " +
            "FROM Pet p GROUP BY p.breed ORDER BY COUNT(p) DESC")
    List<PercentageStatsDTO> getBreedPercentageStats();

    @Query("SELECT COUNT(p) FROM Pet p WHERE p.adoptionStatus = 'ADOPTED'")
    Long countAdoptedPets();


    @Query("SELECT p.location FROM Pet p GROUP BY p.location ORDER BY COUNT(p) DESC")
    List<String> findMostPopularLocation();
}
