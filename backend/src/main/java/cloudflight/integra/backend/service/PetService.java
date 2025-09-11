package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.Pet;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface PetService {
    Pet savePet(Pet pet);
    Pet getPetById(int id);
    List<Pet> getAllPets();

    void deletePet(Pet pet);
    void updatePet(Pet pet);

    List<Pet> filterPets(String species, String breed, Sort sort);
}
