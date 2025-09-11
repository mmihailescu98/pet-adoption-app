package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.repository.PetRepository;
import cloudflight.integra.backend.service.PetService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetServiceImpl implements PetService {
    PetRepository petRepository;


    PetServiceImpl (PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    @Override
    public Pet savePet(Pet pet) {
        return petRepository.save(pet);
    }

    @Override
    public Pet getPetById(int id) {
        return petRepository.findById(id).orElse(null);
    }

    @Override
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    @Override
    public void deletePet(Pet pet) {
        petRepository.delete(pet);
    }

    @Override
    public void updatePet(Pet pet) {
        petRepository.save(pet);
    }

    @Override
    public List<Pet> filterPets(String species, String breed, Sort sort) {
        return petRepository.findPetsBySpeciesIgnoreCaseOrBreedIgnoreCase(species, breed, sort);
    }
}
