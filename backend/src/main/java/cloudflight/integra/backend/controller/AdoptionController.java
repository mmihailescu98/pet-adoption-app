package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.dto.AdoptionAddRequestDTO;
import cloudflight.integra.backend.dto.AdoptionListItemDTO;
import cloudflight.integra.backend.mapper.AdoptionMapper;
import cloudflight.integra.backend.model.User;
import cloudflight.integra.backend.service.AdoptionService;
import cloudflight.integra.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api")
@RestController()
public class AdoptionController {

    @Autowired
    AdoptionService adoptionService;
    @Autowired
    UserService userService;


    @GetMapping(value="/adoptions",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AdoptionListItemDTO>> getAdoptions(@RequestParam(required = false) Integer adopterId) {
        if(adopterId == null) {
            List<AdoptionListItemDTO> dtos = AdoptionMapper.INSTANCE.toListItemsFromModels(adoptionService.getPendingAdoptions());
            return ResponseEntity.ok(dtos);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }
    }

//    @PostMapping("/adoptions")
//    public ResponseEntity<?> createAdoptionListing(@RequestBody AdoptionAddRequestDTO addRequestListing) {
//        try{
//            return ResponseEntity.ok(AdoptionMapper.INSTANCE.toListItemFromModel(adoptionService.createAdoption(addRequestListing)));
//        }catch (Exception e){
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @PostMapping("/adoptions")
    public ResponseEntity<?> createAdoptionListing(@RequestBody AdoptionAddRequestDTO addRequestListing) {
        try {
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> user = userService.findByUsername(username);
            System.out.println(username);
            if(user.isPresent() && addRequestListing.publisherId() == null
                    && addRequestListing.pet() != null)
            {
                AdoptionAddRequestDTO aux = new AdoptionAddRequestDTO(
                        addRequestListing.pet(),
                        user.get().getId(),
                        addRequestListing.additionalImages(),
                        addRequestListing.contactNumber()
                );
                return ResponseEntity.ok(AdoptionMapper.INSTANCE.toListItemFromModel(adoptionService.createAdoption(aux)));
            }
            System.out.println("ceva nu a mers");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
