package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.PetModel;

import java.util.List;
import java.util.Optional;

public interface PetService {
    PetModel savePet(PetModel petModel);
    Optional<PetModel> getPetById(int id);
    Optional<PetModel> getPetByName(String name);
    List<PetModel> getAllPets();

    void deletePet(PetModel petModel);
    void updatePet(PetModel petModel);
}
