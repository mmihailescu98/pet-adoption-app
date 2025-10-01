package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.Pet;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface PetService {
    Pet savePet(Pet pet);
    Pet getPetById(int id);

    void deletePetById(int id);
    void deleteAllPets();

    void updatePet(Pet pet);

    List<Pet> getPets(String species, String breed);
}
