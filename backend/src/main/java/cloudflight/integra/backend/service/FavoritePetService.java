package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.FavoritePet;
import cloudflight.integra.backend.model.Pet;

import java.util.List;
import java.util.Set;

public interface FavoritePetService {
    Set<Integer> getUserFavoritePetIds(Long userId);
    List<Pet> getFavoritePetsForUser(Long userId);
    FavoritePet saveFavoritePet(Long userId, Integer petId);
    FavoritePet deleteFavoritePet(Long userId, Integer petId);
}
