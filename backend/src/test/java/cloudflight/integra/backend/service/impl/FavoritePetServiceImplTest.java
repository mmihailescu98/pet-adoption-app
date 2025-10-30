package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.model.FavoritePet;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.model.User;
import cloudflight.integra.backend.repository.FavoritePetRepository;
import cloudflight.integra.backend.repository.PetRepository;
import cloudflight.integra.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoritePetServiceImplTest {

    private FavoritePetRepository favoritePetRepository;
    private PetRepository petRepository;
    private UserRepository userRepository;
    private FavoritePetServiceImpl favoritePetService;

    @BeforeEach
    void setUp() {
        favoritePetRepository = mock(FavoritePetRepository.class);
        petRepository = mock(PetRepository.class);
        userRepository = mock(UserRepository.class);

        favoritePetService = new FavoritePetServiceImpl(favoritePetRepository, petRepository, userRepository);
    }

    @Test
    void testGetUserFavoritePetIds_Success() {
        when(favoritePetRepository.findPetIdsByUserId(10L))
                .thenReturn(Set.of(1, 2));

        Set<Integer> result = favoritePetService.getUserFavoritePetIds(10L);

        assertEquals(Set.of(1, 2), result);
        verify(favoritePetRepository).findPetIdsByUserId(10L);
    }

    @Test
    void testGetUserFavoritePetIds_NoFavoritePets() {
        when(favoritePetRepository.findPetIdsByUserId(20L))
                .thenReturn(Collections.emptySet());

        Set<Integer> result = favoritePetService.getUserFavoritePetIds(20L);

        assertTrue(result.isEmpty());
        verify(favoritePetRepository).findPetIdsByUserId(20L);
    }

    @Test
    void testGetUserFavoritePetIds_NonExistentUserId() {
        when(favoritePetRepository.findPetIdsByUserId(30L))
                .thenReturn(Collections.emptySet());

        Set<Integer> result = favoritePetService.getUserFavoritePetIds(30L);

        assertTrue(result.isEmpty());
        verify(favoritePetRepository).findPetIdsByUserId(30L);
    }

    @Test
    void testGetFavoritePetsForUser() {
        Pet pet1 = new Pet(); pet1.setId(1);
        Pet pet2 = new Pet(); pet2.setId(2);

        when(favoritePetRepository.findPetsByUserId(10L))
                .thenReturn(List.of(pet1, pet2));

        List<Pet> result = favoritePetService.getFavoritePetsForUser(10L);

        assertEquals(2, result.size());
        assertTrue(result.contains(pet1));
        assertTrue(result.contains(pet2));
        verify(favoritePetRepository).findPetsByUserId(10L);
    }

    @Test
    void testSaveFavoritePet() {
        Long userId = 1L;
        Integer petId = 5;

        Pet pet = new Pet(); pet.setId(petId);
        User user = new User(); user.setId(userId);

        when(petRepository.findById(petId)).thenReturn(Optional.of(pet));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(favoritePetRepository.save(any(FavoritePet.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        FavoritePet result = favoritePetService.saveFavoritePet(userId, petId);

        assertEquals(pet, result.getPet());
        assertEquals(user, result.getUser());

        verify(petRepository).findById(petId);
        verify(userRepository).findById(userId);
        verify(favoritePetRepository).save(any(FavoritePet.class));
    }

    @Test
    void testDeleteFavoritePet() {
        Long userId = 1L;
        Integer petId = 5;

        FavoritePet favoritePet = new FavoritePet();
        Pet pet = new Pet(); pet.setId(petId);
        User user = new User(); user.setId(userId);
        favoritePet.setPet(pet);
        favoritePet.setUser(user);

        when(favoritePetRepository.findByUser_IdAndPet_Id(userId, petId))
                .thenReturn(Optional.of(favoritePet));

        FavoritePet result = favoritePetService.deleteFavoritePet(userId, petId);

        assertEquals(favoritePet, result);
        verify(favoritePetRepository).deleteByUser_IdAndPet_Id(userId, petId);
    }

    @Test
    void testSaveFavoritePet_ThrowsWhenPetNotFound() {
        when(petRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () ->
                favoritePetService.saveFavoritePet(1L, 999));
    }

    @Test
    void testSaveFavoritePet_ThrowsWhenUserNotFound() {
        Pet pet = new Pet(); pet.setId(5);
        when(petRepository.findById(5)).thenReturn(Optional.of(pet));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () ->
                favoritePetService.saveFavoritePet(1L, 5));
    }
}
