package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.dto.PetDTO;
import cloudflight.integra.backend.mapper.PetMapper;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.service.FavoritePetService;
import cloudflight.integra.backend.service.PetService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RequestMapping("/api")
@RestController()
public class PetController {
    private final PetService petService;
    private final FavoritePetService favoritePetService;

    PetController(PetService petService, FavoritePetService favoritePetService) {
        this.petService = petService;
        this.favoritePetService = favoritePetService;
    }

    @GetMapping("/test")
    public List<String> testEndpoint() {
        return List.of("hello", "world");
    }


    @GetMapping(value = "/pets", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PetDTO> getPets(@RequestParam(required = false) String species, @RequestParam(required = false) String breed) {
        //JwtUtil.getAuthenticatedUser().getId();//TODO should be discussed if it s ok to use in order to get user id

        List<Pet> pets = petService.getPets(species, breed);
        Set<Integer> favoritePetIds = favoritePetService.getUserFavoritePetIds(1L);//later it should be an id given by the FE or taken from context

        return PetMapper.INSTANCE.petToPetDTOList(pets, favoritePetIds);
    }

    @GetMapping(value = "/pets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PetDTO getPetById(@PathVariable Integer id) {
        //JwtUtil.getAuthenticatedUser().getId();//TODO should be discussed if it s ok to use in order to get user id

        Pet pet = petService.getPetById(id);
        Set<Integer> favoritePetIds = favoritePetService.getUserFavoritePetIds(1L);//same as above

        return PetMapper.INSTANCE.petToPetDTO(pet, favoritePetIds);
    }

    @PostMapping("/pets/{id}/adopt")
    public PetDTO adoptPet(@PathVariable Integer id) {
        return PetMapper.INSTANCE.petToPetDTO(petService.adoptPet(id));
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
