package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.dto.PetDTO;
import cloudflight.integra.backend.mapper.PetMapper;
import cloudflight.integra.backend.service.PetService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/pets/{name}" )
    public List<PetDTO> getPetByName(@PathVariable String name) {
        return PetMapper.INSTANCE.petToPetDTOList(petService.getPetByName(name));
    }

}
