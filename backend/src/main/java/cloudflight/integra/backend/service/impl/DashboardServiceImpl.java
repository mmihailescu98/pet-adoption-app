package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.dto.AdoptionStatsDTO;
import cloudflight.integra.backend.dto.CountStatsDTO;
import cloudflight.integra.backend.dto.PercentageStatsDTO;
import cloudflight.integra.backend.model.StatsType;
import cloudflight.integra.backend.repository.PetRepository;
import cloudflight.integra.backend.service.DashboardService;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<CountStatsDTO> countStats = petRepository.getSpeciesCountStats();
        long totalPetsNumber = getTotalPetsNumber();
        return countStats.stream()
                .map(stat -> new PercentageStatsDTO(
                        StatsType.SPECIES,
                        stat.name(),
                        totalPetsNumber == 0 ? 0 : (stat.count() * 100.0 / totalPetsNumber)
                ))
                .toList();
    }

    @Override
    public List<PercentageStatsDTO> getBreedPercentages() {
        List<CountStatsDTO> countStats = petRepository.getBreedCountStats();
        long totalPetsNumber = getTotalPetsNumber();
        return countStats.stream()
                .map(stat -> new PercentageStatsDTO(
                        StatsType.BREED,
                        stat.name(),
                        totalPetsNumber == 0 ? 0 : (stat.count() * 100.0 / totalPetsNumber)
                ))
                .toList();
    }

    @Override
    public Long getTotalAdoptedPetsNumber() {
        return petRepository.countAdoptedPets();
    }

    @Override
    public Long getTotalPetsNumber() {
        return petRepository.count();
    }

    @Override
    public float getAdoptionRate() {
        long totalPetsNumber = getTotalPetsNumber();
        long adoptedPetsNumber = getTotalAdoptedPetsNumber();
        return totalPetsNumber == 0 ? 0 : (adoptedPetsNumber * 100.0f / totalPetsNumber);
    }

    @Override
    public String mostPopularPetLocation() {
        return petRepository.findMostPopularLocation().stream().findFirst().orElse("No locations found");
    }
}
