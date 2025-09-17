package cloudflight.integra.backend.controller;

import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.service.PetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@WebMvcTest(PetController.class)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PetService petService;

    @Test
    void testGetAllPets() throws Exception {
        List<Pet> pets = List.of(
                new Pet(1, "Dog", "Doberman", "Rex", "Giulesti", "12", "desc", "testimageurl"),
                new Pet(2, "Cat", "Siamese", "Luna", "Militari", "3", "playful cat", "catimageurl"),
                new Pet(3, "Dog", "Labrador", "Buddy", "Pipera", "5", "friendly dog", "dogimageurl"),
                new Pet(4, "Bird", "Parrot", "Kiwi", "Cotroceni", "2", "talks a lot", "parrotimageurl"),
                new Pet(5, "Dog", "German Shepherd", "Max", "Rahova", "7", "loyal guard dog", "shepherdimageurl"),
                new Pet(6, "Rabbit", "Holland Lop", "Snowball", "Berceni", "1", "small and fluffy", "rabbitimageurl")
                );
        given(petService.getAllPets()).willReturn(pets);

        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Rex"))
                .andExpect(jsonPath("$[0].species").value("Dog"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Luna"))
                .andExpect(jsonPath("$[1].species").value("Cat"))
                .andExpect(jsonPath("$.length()").value(6));
    }

    @Test
    void testGetPetById() throws Exception {
        Pet pet = new Pet(9, "Turtle", "Red-Eared Slider", "Shelly", "Obor", "10", "slow but steady", "turtleimageurl");
        given(petService.getPetById(9)).willReturn(pet);

        mockMvc.perform(get("/api/pets/9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(9))
                .andExpect(jsonPath("$.name").value("Shelly"))
                .andExpect(jsonPath("$.species").value("Turtle"));
    }

    @Test
    void testGetPetByIdInvalidInput() throws Exception {
        mockMvc.perform(get("/api/pets/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetPetByIdNotFound() throws Exception {
        given(petService.getPetById(999)).willThrow(new NoSuchElementException("Pet not found"));

        mockMvc.perform(get("/api/pets/999"))
                .andExpect(status().isNotFound());
    }
}