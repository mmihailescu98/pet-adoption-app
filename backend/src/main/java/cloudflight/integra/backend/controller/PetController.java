package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.dto.PetDTO;
import cloudflight.integra.backend.mapper.PetMapper;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.security.CustomUserDetails;
import cloudflight.integra.backend.security.JwtUtil;
import cloudflight.integra.backend.service.PetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController()
public class PetController {
    private final PetService petService;

    PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping("/test")
    public List<String> testEndpoint() {
        return List.of("hello", "world");
    }


    @GetMapping(value = "/pets", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PetDTO> getPets(@RequestParam(required = false) String species, @RequestParam(required = false) String breed) {
        return PetMapper.INSTANCE.petToPetDTOList(petService.getPets(species, breed));
    }

    @GetMapping(value = "/pets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PetDTO getPetById(@PathVariable Integer id) {
        return PetMapper.INSTANCE.petToPetDTO(petService.getPetById(id));
    }

    @PostMapping("/pets/{id}/adopt")
    public ResponseEntity<PetDTO> adoptPet(@PathVariable Integer id) {
        CustomUserDetails authUser = JwtUtil.getAuthenticatedUser();

        if(authUser == null)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = authUser.getId();

        Pet adoptedPet;
        try {
            adoptedPet = petService.adoptPet(id, userId);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(PetMapper.INSTANCE.petToPetDTO(adoptedPet));
    }

    @PostMapping("/pets")
    public ResponseEntity<PetDTO> addPet(@RequestBody Pet pet) {
        CustomUserDetails authUser = JwtUtil.getAuthenticatedUser();

        if(authUser == null)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try{
            return ResponseEntity.ok(PetMapper.INSTANCE.petToPetDTO(petService.savePet(pet,authUser)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/pets/{id}")
    public void deletePet(@PathVariable Integer id) {
        petService.deletePetById(id);
    }

    @DeleteMapping("/pets")
    public void deleteAllPets() {
        petService.deleteAllPets();
    }

    @PutMapping(value="/pets/{petId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PetDTO> updatePet(@PathVariable Integer petId, @RequestBody PetDTO pet) {
        CustomUserDetails authUser = JwtUtil.getAuthenticatedUser();
        if (authUser == null)
        {
            return ResponseEntity.status(401).build();
        }

        try{
            PetDTO updatedPet = PetMapper.INSTANCE.petToPetDTO(petService.updatePet(pet,petId,authUser));
            return ResponseEntity.ok(updatedPet);
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
