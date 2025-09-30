package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.model.AdoptionEntry;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.model.UserModel;
import cloudflight.integra.backend.repository.AdoptionRepository;
import cloudflight.integra.backend.repository.PetRepository;
import cloudflight.integra.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdoptionServiceImplTest {

    @Mock
    private AdoptionRepository adoptionRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdoptionServiceImpl adoptionService;

    @Test
    void createAdoption_whenPetDoesNotExist_shouldSavePetAndAdoption() {
        Pet pet = new Pet();
        pet.setId(1);

        UserModel user = new UserModel();
        user.setUsername("john");

        AdoptionEntry entry = new AdoptionEntry();
        entry.setPet(pet);
        entry.setPublisher(user);

        when(petRepository.findById(1)).thenReturn(Optional.empty());
        when(petRepository.save(any(Pet.class))).thenReturn(pet);
        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(adoptionRepository.save(any(AdoptionEntry.class))).thenReturn(entry);

        AdoptionEntry result = adoptionService.createAdoption(entry);

        assertEquals(entry, result);
        verify(petRepository).save(pet);
        verify(adoptionRepository).save(entry);
    }

    @Test
    void createAdoption_whenPetExistsAndAlreadyPending_shouldThrowException() {
        Pet pet = new Pet();
        pet.setId(2);

        UserModel user = new UserModel();
        user.setUsername("alice");

        AdoptionEntry entry = new AdoptionEntry();
        entry.setPet(pet);
        entry.setPublisher(user);

        when(petRepository.findById(2)).thenReturn(Optional.of(pet));
        when(adoptionRepository.findByPetAndAdopterIsNull(pet))
                .thenReturn(Optional.of(new AdoptionEntry()));

        assertThrows(RuntimeException.class, () -> adoptionService.createAdoption(entry));
    }

    @Test
    void createAdoption_whenUserNotFound_shouldThrowException() {
        Pet pet = new Pet();
        pet.setId(3);

        UserModel user = new UserModel();
        user.setUsername("ghost");

        AdoptionEntry entry = new AdoptionEntry();
        entry.setPet(pet);
        entry.setPublisher(user);

        when(petRepository.findById(3)).thenReturn(Optional.of(pet));
        when(adoptionRepository.findByPetAndAdopterIsNull(pet)).thenReturn(Optional.empty());
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> adoptionService.createAdoption(entry));
    }

    @Test
    void getPendingAdoptions_shouldReturnList() {
        AdoptionEntry entry1 = new AdoptionEntry();
        AdoptionEntry entry2 = new AdoptionEntry();

        when(adoptionRepository.findByAdopterIsNull()).thenReturn(List.of(entry1, entry2));

        List<AdoptionEntry> result = adoptionService.getPendingAdoptions();

        assertEquals(2, result.size());
        verify(adoptionRepository).findByAdopterIsNull();
    }
}
