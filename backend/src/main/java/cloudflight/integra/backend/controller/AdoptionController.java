package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.dto.AdoptionAddRequestDTO;
import cloudflight.integra.backend.dto.AdoptionListItemDTO;
import cloudflight.integra.backend.mapper.AdoptionMapper;
import cloudflight.integra.backend.security.JwtUtil;
import cloudflight.integra.backend.service.AdoptionService;
import cloudflight.integra.backend.service.FavoritePetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequestMapping("/api")
@RestController()
public class AdoptionController {

    AdoptionService adoptionService;
    FavoritePetService favoritePetService;

    AdoptionController(AdoptionService adoptionService,
                       FavoritePetService favoritePetService) {
        this.adoptionService = adoptionService;
        this.favoritePetService = favoritePetService;
    }

    @GetMapping(value="/adoptions",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AdoptionListItemDTO>> getAdoptions(@RequestParam(required = false) Integer adopterId) {
        if(adopterId == null) {
            Long userId = JwtUtil.getAuthenticatedUser().getId();
            Set<Integer> favoritePetIds = favoritePetService.getUserFavoritePetIds(userId);

            List<AdoptionListItemDTO> dtos = AdoptionMapper.INSTANCE.toListItemsFromModels(adoptionService.getPendingAdoptions(),favoritePetIds);
            return ResponseEntity.ok(dtos);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
    }

    @PostMapping("/adoptions")
    public ResponseEntity<?> createAdoptionListing(@RequestBody AdoptionAddRequestDTO addRequestListing) {
        try{
            return ResponseEntity.ok(AdoptionMapper.INSTANCE.toListItemFromModel(adoptionService.createAdoption(addRequestListing)));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(value = "/adoptions/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> hasUserRequestedAdoption(
            @RequestParam Integer petId,
            @RequestParam Long userId) {
        boolean hasRequested = adoptionService.hasUserRequestedAdoption(petId, userId);
        return ResponseEntity.ok(hasRequested);
    }

}
