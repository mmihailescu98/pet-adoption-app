package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.dto.PetDTO;
import cloudflight.integra.backend.mapper.PetMapper;
import cloudflight.integra.backend.service.PetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

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

    @GetMapping("/pets")
    public List<PetDTO> getAllPets() {
        return PetMapper.INSTANCE.petToPetDTOList(petService.getAllPets());
    }

    @GetMapping("/pets/{id}")
    public PetDTO getPetById(@PathVariable Integer id) {
        return PetMapper.INSTANCE.petToPetDTO(petService.getPetById(id));
    }

    @PostMapping("/pets/{id}/adopt")
    public PetDTO adoptPet(@PathVariable Integer id) {
        return PetMapper.INSTANCE.petToPetDTO(petService.adoptPet(id));
    }

}
