package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.Pet;
import java.util.List;

public interface PetService {
  
    Pet savePet(Pet pet);
    Pet getPetById(int id);

    void deletePetById(int id);
    void deleteAllPets();

    void updatePet(Pet pet);

    Pet adoptPet(int id);

    List<Pet> getPets(String species, String breed);
}
