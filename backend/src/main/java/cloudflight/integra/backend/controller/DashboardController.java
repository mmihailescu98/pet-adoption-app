package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.dto.AdoptionStatsDTO;
import cloudflight.integra.backend.dto.PercentageStatsDTO;
import cloudflight.integra.backend.service.DashboardService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/dashboard")
@RestController()
public class DashboardController {
    private final DashboardService dashboardService;

    DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping(value = "/species/adopted", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AdoptionStatsDTO> getMostAdoptedSpecies() {
        return dashboardService.getMostAdoptedSpecies();
    }

    @GetMapping(value = "/breeds/adopted", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AdoptionStatsDTO> getMostAdoptedBreeds() {
        return dashboardService.getMostAdoptedBreeds();
    }

    @GetMapping(value = "/species/percentages", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PercentageStatsDTO> getSpeciesPercentages() {
        return dashboardService.getSpeciesPercentages();
    }

    @GetMapping( value = "/breeds/percentages", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PercentageStatsDTO> getBreedPercentages() {
        return dashboardService.getBreedPercentages();
    }

    @GetMapping(value = "/adopted-pets/total", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getTotalAdoptedPets() {
        return dashboardService.getTotalAdoptedPetsNumber();
    }

    @GetMapping(value = "/pets/total", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long getTotalPets() {
        return dashboardService.getTotalPetsNumber();
    }

    @GetMapping(value = "/adoptions/rate", produces = MediaType.APPLICATION_JSON_VALUE)
    public float getAdoptionRate() {
        return dashboardService.getAdoptionRate();
    }

    @GetMapping(value = "/locations/most-popular", produces = MediaType.APPLICATION_JSON_VALUE)
    public String mostPopularPetLocation() {
        return dashboardService.mostPopularPetLocation();
    }
}
