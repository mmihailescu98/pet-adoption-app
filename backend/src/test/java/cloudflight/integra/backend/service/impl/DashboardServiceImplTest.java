package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.dto.AdoptionStatsDTO;
import cloudflight.integra.backend.dto.PercentageStatsDTO;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.model.StatsType;
import cloudflight.integra.backend.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DashboardServiceImplTest {

    private PetRepository petRepository;
    private DashboardServiceImpl dashboardService;

    @BeforeEach
    void setUp() {
        petRepository = mock(PetRepository.class);
        dashboardService = new DashboardServiceImpl(petRepository);
    }

    @Test
    void getMostAdoptedSpecies() {
        List<AdoptionStatsDTO> mockStats = List.of(
                new AdoptionStatsDTO(StatsType.SPECIES, "Dog", 15),
                new AdoptionStatsDTO(StatsType.SPECIES, "Cat", 10),
                new AdoptionStatsDTO(StatsType.SPECIES, "Rabbit", 5)
        );
        when(petRepository.getAdoptedSpeciesStats()).thenReturn(mockStats);

        List<AdoptionStatsDTO> result = dashboardService.getMostAdoptedSpecies();

        assertEquals(3, result.size());
        assertEquals("Dog", result.get(0).name());
        assertEquals(15, result.get(0).adoptedCount());
        assertEquals("Cat", result.get(1).name());
        assertEquals(10, result.get(1).adoptedCount());
        assertEquals("Rabbit", result.get(2).name());
        assertEquals(5, result.get(2).adoptedCount());
        verify(petRepository).getAdoptedSpeciesStats();
    }

    @Test
    void getMostAdoptedBreeds() {
        List<AdoptionStatsDTO> mockStats = List.of(
                new AdoptionStatsDTO(StatsType.BREED, "Labrador", 8),
                new AdoptionStatsDTO(StatsType.BREED, "Poodle", 7),
                new AdoptionStatsDTO(StatsType.BREED, "Bulldog", 6),
                new AdoptionStatsDTO(StatsType.BREED, "Beagle", 5),
                new AdoptionStatsDTO(StatsType.BREED, "Boxer", 4),
                new AdoptionStatsDTO(StatsType.BREED, "Dachshund", 3)
        );
        when(petRepository.getAdoptedBreedStats()).thenReturn(mockStats);

        List<AdoptionStatsDTO> result = dashboardService.getMostAdoptedBreeds();

        assertEquals(5, result.size());
        assertEquals("Labrador", result.get(0).name());
        assertEquals("Boxer", result.get(4).name());
        verify(petRepository).getAdoptedBreedStats();
    }

    @Test
    void getSpeciesPercentages() {
        List<PercentageStatsDTO> mockStats = List.of(
                new PercentageStatsDTO(StatsType.SPECIES, "Cat", 40.0),
                new PercentageStatsDTO(StatsType.SPECIES, "Dog", 60.0)
        );
        when(petRepository.getSpeciesPercentageStats()).thenReturn(mockStats);

        List<PercentageStatsDTO> result = dashboardService.getSpeciesPercentages();

        assertEquals(2, result.size());
        assertEquals("Cat", result.get(0).name());
        assertEquals(40.0, result.get(0).percentage());
        assertEquals("Dog", result.get(1).name());
        assertEquals(60.0, result.get(1).percentage());
        verify(petRepository).getSpeciesPercentageStats();
    }

    @Test
    void getBreedPercentages() {
        List<PercentageStatsDTO> mockStats = List.of(
                new PercentageStatsDTO(StatsType.BREED, "Siamese", 0.0),
                new PercentageStatsDTO(StatsType.BREED, "Persian", 100.0)
        );
        when(petRepository.getBreedPercentageStats()).thenReturn(mockStats);

        List<PercentageStatsDTO> result = dashboardService.getBreedPercentages();

        assertEquals(2, result.size());
        assertEquals("Siamese", result.get(0).name());
        assertEquals(0.0, result.get(0).percentage());
        assertEquals("Persian", result.get(1).name());
        assertEquals(100.0, result.get(1).percentage());
        verify(petRepository).getBreedPercentageStats();
    }

    @Test
    void getTotalAdoptedPets() {
        when(petRepository.countAdoptedPets()).thenReturn(7L);
        assertEquals(7L, dashboardService.getTotalAdoptedPets());
        verify(petRepository).countAdoptedPets();
    }

    @Test
    void getTotalPets() {
        when(petRepository.count()).thenReturn(20L);
        assertEquals(20L, dashboardService.getTotalPets());
        verify(petRepository).count();
    }

    @Test
    void getAdoptionRate() {
        when(petRepository.count()).thenReturn(10L);
        when(petRepository.countAdoptedPets()).thenReturn(5L);
        assertEquals(50.0f, dashboardService.getAdoptionRate());
    }

    @Test
    void mostPopularPetLocation() {
        when(petRepository.findMostPopularLocation()).thenReturn(List.of("Berlin"));
        assertEquals("Berlin", dashboardService.mostPopularPetLocation());
        when(petRepository.findMostPopularLocation()).thenReturn(List.of());
        assertEquals("No locations found", dashboardService.mostPopularPetLocation());
    }
}