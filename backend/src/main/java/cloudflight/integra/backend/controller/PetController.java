package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.dto.PetDTO;
import cloudflight.integra.backend.mapper.PetMapper;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.service.PetService;
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

    @GetMapping("/pets")
    public List<PetDTO> getPets(@RequestParam(required = false) String species, @RequestParam(required = false) String breed) {
        return PetMapper.INSTANCE.petToPetDTOList(petService.getPets(species, breed));
    }

    @GetMapping("/pets/{id}")
    public PetDTO getPetById(@PathVariable Integer id) {
        return PetMapper.INSTANCE.petToPetDTO(petService.getPetById(id));
    }

    @PostMapping("/pets")
    public PetDTO addPet(@RequestBody Pet pet) {
        return PetMapper.INSTANCE.petToPetDTO(petService.savePet(pet));
    }

    @DeleteMapping("/pets/{id}")
    public void deletePet(@PathVariable Integer id) {
        petService.deletePetById(id);
    }

    @DeleteMapping("/pets")
    public void deleteAllPets() {
        petService.deleteAllPets();
    }
}
