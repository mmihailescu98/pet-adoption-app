package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.model.PetModel;
import cloudflight.integra.backend.repository.PetRepository;
import cloudflight.integra.backend.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetServiceImpl implements PetService {
    PetRepository petRepository;

    @Autowired
    public void setPetRepository(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public PetModel savePet(PetModel petModel) {
        return petRepository.savePet(petModel);
    }

    @Override
    public Optional<PetModel> getPetById(int id) {
        return petRepository.getPetById(id);
    }

    @Override
    public Optional<PetModel> getPetByName(String name) {
        return petRepository.getPetByName(name);
    }

    @Override
    public List<PetModel> getAllPets() {
        return petRepository.getAllPets();
    }

    @Override
    public void deletePet(PetModel petModel) {
        petRepository.deletePet(petModel);
    }

    @Override
    public void updatePet(PetModel petModel) {
        petRepository.updatePet(petModel);
    }
}
