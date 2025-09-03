package cloudflight.integra.backend.repository;

import cloudflight.integra.backend.model.PetModel;

import java.util.List;
import java.util.Optional;

public interface PetRepository {
    PetModel savePet(PetModel petModel);
    Optional<PetModel> getPetById(int id);
    Optional<PetModel> getPetByName(String name);
    List<PetModel> getAllPets();
}
