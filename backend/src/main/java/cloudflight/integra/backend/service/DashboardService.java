package cloudflight.integra.backend.service;

import cloudflight.integra.backend.dto.AdoptionStatsDTO;
import cloudflight.integra.backend.dto.PercentageStatsDTO;
import cloudflight.integra.backend.dto.PetDTO;

import java.util.List;

public interface DashboardService {

    List<AdoptionStatsDTO> getMostAdoptedSpecies();

    List<AdoptionStatsDTO> getMostAdoptedBreeds();

    List<PercentageStatsDTO> getSpeciesPercentages();

    List<PercentageStatsDTO> getBreedPercentages();

    Long getTotalAdoptedPets();

    Long getTotalPets();

    float getAdoptionRate();

    String mostPopularPetLocation();
}
