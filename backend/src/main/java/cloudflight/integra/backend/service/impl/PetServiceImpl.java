package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.dto.PetDTO;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.model.PetStatus;
import cloudflight.integra.backend.model.User;
import cloudflight.integra.backend.repository.PetRepository;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.PetService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetServiceImpl implements PetService {
    PetRepository petRepository;
    UserRepository userRepository;

    PetServiceImpl (PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
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
    public Pet updatePet(PetDTO pet) throws Exception {
        Pet existingPet = getPetById(pet.id());
        if (existingPet == null) {
            throw new Exception("Pet not found with id: " + pet.id());
        }

        existingPet.setName(pet.name());
        existingPet.setSpecies(pet.species());
        existingPet.setBreed(pet.breed());
        existingPet.setAge(pet.age());
        existingPet.setLocation(pet.location());
        existingPet.setDescription(pet.description());
        existingPet.setImgURL(pet.imgURL());

        petRepository.save(existingPet);

        return existingPet;
    }

    @Override
    public Pet adoptPet(int petId, long userId) throws Exception {
        Pet pet = getPetById(petId);
        if (pet == null) {
            throw new Exception("Pet not found with id " + petId);
        }

        User newOwner = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found with id " + userId));

        pet.setOwner(newOwner);
        pet.setStatus(PetStatus.ADOPTED);
        return pet;
    }

    @Override
    public List<Pet> getPets(String species, String breed) {
        Sort sort = Sort.by("name").ascending();
        return petRepository.filterPets(species, breed, sort);
    }

    @Override
    public Boolean isOwnerOfPet(long userId, int petId) throws Exception {
        Optional<Pet> pet = petRepository.findById(petId);

        if (pet.isEmpty())
            throw new Exception("Pet with id " + petId + " not found.");

        if (pet.get().getOwner() == null)
            throw new Exception("Pet with id " + petId + " has no owner.?");

        if (pet.get().getOwner().getId() != userId)
            return false;

        return true;
    }
}
