package cloudflight.integra.backend.mapper;

import cloudflight.integra.backend.dto.PetDTO;
import cloudflight.integra.backend.model.Pet;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PetMapperTest {

    private final PetMapper mapper = PetMapper.INSTANCE;

    @Test
    void testIsFavorite_ReturnsTrueWhenIdInSet() {
        Pet pet = new Pet();
        pet.setId(10);
        Set<Integer> favorites = Set.of(10, 20, 30);

        boolean result = mapper.isFavorite(pet, favorites);

        assertTrue(result);
    }

    @Test
    void testIsFavorite_ReturnsFalseWhenIdNotInSet() {
        Pet pet = new Pet();
        pet.setId(5);
        Set<Integer> favorites = Set.of(10, 20, 30);

        boolean result = mapper.isFavorite(pet, favorites);

        assertFalse(result);
    }

    @Test
    void testPetToPetDTO_WithFavorite() {
        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Buddy");

        Set<Integer> favorites = Set.of(1, 2, 3);

        PetDTO dto = mapper.petToPetDTO(pet, favorites);

        assertEquals(pet.getId(), dto.id());
        assertEquals(pet.getName(), dto.name());
        assertTrue(dto.isUserFavorite());
    }

    @Test
    void testPetToPetDTO_WithNonFavorite() {
        Pet pet = new Pet();
        pet.setId(4);
        pet.setName("Max");

        Set<Integer> favorites = Set.of(1, 2, 3);

        PetDTO dto = mapper.petToPetDTO(pet, favorites);

        assertEquals(pet.getId(), dto.id());
        assertEquals(pet.getName(), dto.name());
        assertFalse(dto.isUserFavorite());
    }

    @Test
    void testPetToPetDTOList_MixedFavorites() {
        Pet pet1 = new Pet(); pet1.setId(1); pet1.setName("Bella");
        Pet pet2 = new Pet(); pet2.setId(2); pet2.setName("Charlie");
        Pet pet3 = new Pet(); pet3.setId(3); pet3.setName("Luna");

        List<Pet> pets = List.of(pet1, pet2, pet3);
        Set<Integer> favorites = Set.of(1, 3);

        List<PetDTO> dtos = mapper.petToPetDTOList(pets, favorites);

        assertEquals(3, dtos.size());
        assertTrue(dtos.get(0).isUserFavorite());
        assertFalse(dtos.get(1).isUserFavorite());
        assertTrue(dtos.get(2).isUserFavorite());
    }
}
