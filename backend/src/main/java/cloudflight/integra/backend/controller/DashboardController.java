package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.dto.AdoptionStatsDTO;
import cloudflight.integra.backend.dto.PercentageStatsDTO;
import cloudflight.integra.backend.service.DashboardService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("http://localhost:4200")
@RequestMapping("/api/dashboard")
@RestController()
public class DashboardController {
    private final DashboardService dashboardService;

    DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/species/adopted")
    public List<AdoptionStatsDTO> getMostAdoptedSpecies() {
        return dashboardService.getMostAdoptedSpecies();
    }

    @GetMapping("/breeds/adopted")
    public List<AdoptionStatsDTO> getMostAdoptedBreeds() {
        return dashboardService.getMostAdoptedBreeds();
    }

    @GetMapping("/species/percentages")
    public List<PercentageStatsDTO> getSpeciesPercentages() {
        return dashboardService.getSpeciesPercentages();
    }

    @GetMapping("/breeds/percentages")
    public List<PercentageStatsDTO> getBreedPercentages() {
        return dashboardService.getBreedPercentages();
    }

    @GetMapping("/total-adopted-pets")
    public Long getTotalAdoptedPets() {
        return dashboardService.getTotalAdoptedPets();
    }

    @GetMapping("/total-pets")
    public Long getTotalPets() {
        return dashboardService.getTotalPets();
    }

    @GetMapping("/adoption-rate")
    public float getAdoptionRate() {
        return dashboardService.getAdoptionRate();
    }

    @GetMapping("/locations/most-popular")
    public String mostPopularPetLocation() {
        return dashboardService.mostPopularPetLocation();
    }
}
