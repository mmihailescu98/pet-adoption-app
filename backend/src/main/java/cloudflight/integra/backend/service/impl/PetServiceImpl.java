package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.model.UserModel;
import cloudflight.integra.backend.repository.PetRepository;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.PetService;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;


import java.util.List;

@Service
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;
    private final UserRepository UserRepository;

    public PetServiceImpl(PetRepository petRepository, UserRepository userRepository) {
        this.petRepository = petRepository;
        this.UserRepository = userRepository;
    }


    @Override
    public Pet savePet(Pet pet) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            String username = auth.getName();
            UserModel creator = UserRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
            pet.setCreatedBy(creator);
        }
        return petRepository.save(pet);
    }


    @Override
    public Pet getPetById(int id) {
        return petRepository.findById(id).orElse(null);
    }

    @Override
    public void deletePetById(int id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found with ID: " + id));

        if (!isAdmin) {
            if (pet.getCreatedBy() == null ||
                    pet.getCreatedBy().getUsername() == null ||
                    !pet.getCreatedBy().getUsername().equals(username)) {
                throw new RuntimeException("You are not authorized to delete this pet.");
            }
        }

        petRepository.delete(pet);
    }



    @Override
    public void deleteAllPets() {
        petRepository.deleteAll();
    }

    @Override
    public void updatePet(Pet pet) {
        petRepository.save(pet);
    }

    @Override
    public List<Pet> getPets(String species, String breed) {
        Sort sort = Sort.by("name").ascending();
        return petRepository.filterPets(species, breed, sort);
    }
}
