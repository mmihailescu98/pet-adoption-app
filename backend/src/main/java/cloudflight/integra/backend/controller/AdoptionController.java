package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.dto.AdoptionAddRequestDTO;
import cloudflight.integra.backend.mapper.AdoptionMapper;
import cloudflight.integra.backend.service.AdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@CrossOrigin("http://localhost:4200")
@RequestMapping("/api")
@RestController()
public class AdoptionController {

    @Autowired
    AdoptionService adoptionService;

// will work on later
//    @GetMapping("/adoptionlistings")
//    public ResponseEntity<List<AdoptionListing>> getAdoptionListings() {
//
//    }

    @PostMapping("/adoptionlistings")
    public ResponseEntity<?> createAdoptionListing(@RequestBody AdoptionAddRequestDTO addRequestListing) {
        try{
            var mappedEntity = AdoptionMapper.INSTANCE.toModelFromAddRequest(addRequestListing);
            var addedEntity = adoptionService.createAdoption(mappedEntity);

            URI savedPath = URI.create("/api/adoptionlistings/" + addedEntity.getId());
            return ResponseEntity.created(savedPath).build();
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
