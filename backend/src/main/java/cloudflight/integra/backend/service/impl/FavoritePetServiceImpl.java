package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.model.FavoritePet;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.model.User;
import cloudflight.integra.backend.repository.FavoritePetRepository;
import cloudflight.integra.backend.repository.PetRepository;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.FavoritePetService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FavoritePetServiceImpl implements FavoritePetService {
    FavoritePetRepository favoritePetRepository;
    PetRepository petRepository;
    UserRepository userRepository;

    public FavoritePetServiceImpl(
            FavoritePetRepository favoritePetRepository,
            PetRepository petRepository,
            UserRepository userRepository
    ) {
        this.favoritePetRepository = favoritePetRepository;
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieves a set with the id s of the pets that are the user's favorite
     * @param userId id of the user whose favorite pets are to be retrieved
     * @return Set of pet ids that are the user's favorite
     */
    @Override
    public Set<Integer> getUserFavoritePetIds(Long userId) {
        //In Progress
        Set<Integer> favoritePetsIds = new HashSet<>();
        for (FavoritePet favorite : favoritePetRepository.findByUser_Id(userId))
        {
            favoritePetsIds.add(favorite.getPet().getId());
        }
        return favoritePetsIds;
    }

    @Override
    public List<Pet> getFavoritePetsForUser(Long userId) {
        List<Pet> favoritePets = new ArrayList<>();
        for (FavoritePet favorite : favoritePetRepository.findByUser_Id(userId))
        {
            favoritePets.add(favorite.getPet());
        }

        return favoritePets;
    }

    @Override
    public FavoritePet saveFavoritePet(Long userId, Integer petId) {
        Pet pet  = petRepository.findById(petId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();

        FavoritePet toBeAdded = new FavoritePet();
        toBeAdded.setPet(pet);
        toBeAdded.setUser(user);

        return favoritePetRepository.save(toBeAdded);
    }

    @Override
    @Transactional
    public FavoritePet deleteFavoritePet(Long userId, Integer petId) {
        FavoritePet deleted = favoritePetRepository.findByUser_IdAndPet_Id(userId, petId).orElseThrow();
        favoritePetRepository.deleteByUser_IdAndPet_Id(userId, petId);
        return deleted;
    }
}
