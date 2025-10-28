package cloudflight.integra.backend.service;

import cloudflight.integra.backend.dto.PetDTO;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.security.CustomUserDetails;

import java.util.List;

public interface PetService {
  
    Pet savePet(Pet pet, CustomUserDetails authUser) throws Exception;
    Pet getPetById(int id);

    void deletePetById(int id);
    void deleteAllPets();

    Pet updatePet(PetDTO pet, Integer petId, CustomUserDetails authUser) throws Exception;

    Pet adoptPet(int petId, long userId) throws Exception;

    List<Pet> getPets(String species, String breed);

    Boolean isOwnerOfPet(long userId, int petId) throws Exception;
}
