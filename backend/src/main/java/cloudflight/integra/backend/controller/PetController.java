package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.dto.PetDTO;
import cloudflight.integra.backend.mapper.PetMapper;
import cloudflight.integra.backend.service.PetService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping(value = "/pets", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PetDTO> getAllPets() {
        return PetMapper.INSTANCE.petToPetDTOList(petService.getAllPets());
    }

    @GetMapping(value = "/pets/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PetDTO getPetById(@PathVariable Integer id) {
        return PetMapper.INSTANCE.petToPetDTO(petService.getPetById(id));
    }
}
