package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.dto.PetDTO;
import cloudflight.integra.backend.mapper.LocationMapper;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.model.PetStatus;
import cloudflight.integra.backend.model.User;
import cloudflight.integra.backend.repository.PetRepository;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.security.CustomUserDetails;
import cloudflight.integra.backend.service.PetService;
import cloudflight.integra.backend.validators.PetValidator;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PetServiceImpl implements PetService {
    PetRepository petRepository;
    UserRepository userRepository;

    PetValidator petValidator;

    PetServiceImpl (PetRepository petRepository, UserRepository userRepository, PetValidator petValidator) {
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.petValidator = petValidator;
    }

    @Override
    public Pet savePet(Pet pet, CustomUserDetails authUser) throws Exception {

        if(!Objects.equals(authUser.getId(), pet.getOwner().getId()))
        {
            throw new Exception("Authenticated user doesnt match pet owner");
        }

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
    public Pet updatePet(PetDTO pet, Integer petId, CustomUserDetails authUser) throws Exception {
        if(!pet.id().equals(petId))
        {
            throw new Exception("Pet id doesnt match request path id");
        }

        boolean isAdmin = false;
        boolean isPetOwner;
        if (authUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN")))
        {
            isAdmin = true;
        }

        isPetOwner = this.isOwnerOfPet(authUser.getId(), petId);

        if (!isAdmin && !isPetOwner ){
            throw new Exception("User is neither pet owner or an admin!");
        }

        Pet existingPet = getPetById(pet.id());
        if (existingPet == null) {
            throw new Exception("Pet not found with id: " + pet.id());
        }

        existingPet.setName(pet.name());
        existingPet.setSpecies(pet.species());
        existingPet.setBreed(pet.breed());
        existingPet.setAge(pet.age());
        existingPet.setLocation(LocationMapper.INSTANCE.locationDTOToLocation(pet.location()));
        existingPet.setDescription(pet.description());
        existingPet.setImgURL(pet.imgURL());

        Errors errors = new BeanPropertyBindingResult(existingPet, "pet");
        petValidator.validate(existingPet, errors);

        if (errors.hasErrors()) {
            StringBuilder errorMsg = new StringBuilder("Validation failed: ");
            for (ObjectError error : errors.getAllErrors()) {
                errorMsg.append(error.getDefaultMessage()).append("; ");
            }
            throw new IllegalArgumentException(errorMsg.toString());
        }

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
        {
            throw new Exception("Pet with id " + petId + " not found.");
        }

        if (pet.get().getOwner() == null)
        {
            throw new Exception("Pet with id " + petId + " has no owner.?");
        }

        return pet.get().getOwner().getId() == userId;
    }
}
