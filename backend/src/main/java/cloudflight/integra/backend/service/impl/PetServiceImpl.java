package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.model.PetStatus;
import cloudflight.integra.backend.repository.PetRepository;
import cloudflight.integra.backend.security.JwtUtil;
import cloudflight.integra.backend.service.PetService;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetServiceImpl implements PetService {
    private final PetRepository petRepository;

    PetServiceImpl(PetRepository petRepository) {
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
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        var currentUser = JwtUtil.getAuthenticatedUser();

        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        boolean isOwner = pet.getCreatedBy() != null &&
                pet.getCreatedBy().getId().equals(currentUser.getId());

        if (!isAdmin && !isOwner) {
            throw new AccessDeniedException("You are not allowed to delete this pet");
        }

        petRepository.deleteById(id);
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
    public Pet adoptPet(int id) {
        Pet pet = getPetById(id);
        if (pet != null) {
            pet.setStatus(PetStatus.ADOPTED);
            updatePet(pet);
        }
        return pet;
    }

    @Override
    public List<Pet> getPets(String species, String breed) {
        Sort sort = Sort.by("name").ascending();
        return petRepository.filterPets(species, breed, sort);
    }
}
