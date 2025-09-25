package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.dto.AdoptionStatsDTO;
import cloudflight.integra.backend.dto.PercentageStatsDTO;
import cloudflight.integra.backend.dto.PetDTO;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.model.StatsType;
import cloudflight.integra.backend.repository.PetRepository;
import cloudflight.integra.backend.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final PetRepository petRepository;

    DashboardServiceImpl(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public List<AdoptionStatsDTO> getMostAdoptedSpecies() {
        return petRepository.getAdoptedSpeciesStats().stream().limit(5).toList();
    }

    @Override
    public List<AdoptionStatsDTO> getMostAdoptedBreeds() {
        return petRepository.getAdoptedBreedStats().stream().limit(5).toList();
    }

    @Override
    public List<PercentageStatsDTO> getSpeciesPercentages() {
        return petRepository.getSpeciesPercentageStats().stream().toList();
    }

    @Override
    public List<PercentageStatsDTO> getBreedPercentages() {
        return petRepository.getBreedPercentageStats().stream().toList();
    }

    @Override
    public Long getTotalAdoptedPets() {
        return petRepository.countAdoptedPets();
    }

    @Override
    public Long getTotalPets() {
        return petRepository.count();
    }

    @Override
    public float getAdoptionRate() {
        long totalPets = getTotalPets();
        long adoptedPets = getTotalAdoptedPets();
        if (totalPets == 0) {
            return 0;
        }
        return (adoptedPets * 100.0f) / totalPets;
    }

    @Override
    public String mostPopularPetLocation() {
        return petRepository.findMostPopularLocation().stream().findFirst().orElse("No locations found");
    }
}
