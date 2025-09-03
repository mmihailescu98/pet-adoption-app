package cloudflight.integra.backend.controller;


import cloudflight.integra.backend.SwaggerTestModel.SwagPetDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TestController {

    @GetMapping("/test")
    public List<String> testEndpoint() {
        return List.of("hello", "world");
    }

    @GetMapping("/test/swagpets")
    public List<SwagPetDTO> getSwagPets()
    {
        List<SwagPetDTO> list = new ArrayList<>();
        list.add(new SwagPetDTO(1, "Pig", "itsapig"));
        list.add(new SwagPetDTO(2, "Dog", "itsadog"));
        list.add(new SwagPetDTO(3, "Cat", "ugetit"));
        return list;
    }
}
