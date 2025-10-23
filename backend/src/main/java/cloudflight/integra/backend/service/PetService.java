package cloudflight.integra.backend.service;

import cloudflight.integra.backend.dto.PetDTO;
import cloudflight.integra.backend.model.Pet;

import java.util.List;

public interface PetService {
  
    Pet savePet(Pet pet);
    Pet getPetById(int id);

    void deletePetById(int id);
    void deleteAllPets();

    Pet updatePet(PetDTO pet) throws Exception;

    Pet adoptPet(int petId, long userId) throws Exception;

    List<Pet> getPets(String species, String breed);

    Boolean isOwnerOfPet(long userId, int petId) throws Exception;
}
