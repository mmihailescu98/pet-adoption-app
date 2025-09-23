package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.repository.PetRepository;
import cloudflight.integra.backend.service.PetService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static cloudflight.integra.backend.service.validators.PetValidator.validatePet;

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
    public void deletePetById(int id) {
        petRepository.deleteById(id);
    }

    @Override
    public void deleteAllPets() {
        petRepository.deleteAll();
    }

    @Override
    public void updatePet(Pet pet) {
        validatePet(pet.getBreed());

        petRepository.save(pet);
    }

    @Override
    public List<Pet> getPets(String species, String breed) {
        Sort sort = Sort.by("name").ascending();
        return petRepository.filterPets(species, breed, sort);
    }
}
