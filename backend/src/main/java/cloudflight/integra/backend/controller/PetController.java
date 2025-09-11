package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.dto.PetDTO;
import cloudflight.integra.backend.mapper.PetMapper;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.service.PetService;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:4200")
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

    @GetMapping("/pets/{name}" )
    public List<PetDTO> getPetByName(@PathVariable String name) {
        return PetMapper.INSTANCE.petToPetDTOList(petService.getPetByName(name));
    }

    @GetMapping("/pets")
    public List<PetDTO> getAllPets() {
        return PetMapper.INSTANCE.petToPetDTOList(petService.getAllPets());
    }

    @GetMapping("/pets/{id}")
    public PetDTO getPetById(@PathVariable Integer id) {
        return PetMapper.INSTANCE.petToPetDTO(petService.getPetById(id).orElseThrow());
    }

    @GetMapping("/pets/filter")
    public List<PetDTO> filterPets(@RequestParam String species, @RequestParam String breed) {
        return PetMapper.INSTANCE.petToPetDTOList(petService.filterPets(species, breed, Sort.by("name").ascending()));
    }

    @PostMapping("/pets/add")
    public PetDTO addPet(@RequestBody Pet pet) {
        return PetMapper.INSTANCE.petToPetDTO(petService.savePet(pet));
    }

    @DeleteMapping("/pets/delete/{id}")
    public void deletePet(@PathVariable Integer id) {
        Pet pet = petService.getPetById(id).orElseThrow();
        petService.deletePet(pet);
    }

    @DeleteMapping("/pets/delete")
    public void deleteAllPets() {
        petService.getAllPets().forEach(petService::deletePet);
    }

}
