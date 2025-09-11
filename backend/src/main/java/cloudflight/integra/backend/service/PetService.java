package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.Pet;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface PetService {
    Pet savePet(Pet pet);
    Optional<Pet> getPetById(int id);
    List<Pet> getPetByName(String name);
    List<Pet> getAllPets();

    void deletePet(Pet pet);
    void updatePet(Pet pet);

    List<Pet> filterPets(String species, String breed, Sort sort);
}
