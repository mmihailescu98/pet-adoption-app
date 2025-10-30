package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.dto.AdoptionAddRequestDTO;
import cloudflight.integra.backend.model.AdoptionEntry;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.model.PetStatus;
import cloudflight.integra.backend.model.User;
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
import static org.mockito.ArgumentMatchers.any;
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
    pet.setName("Buddy");
    pet.setStatus(PetStatus.WAITING);

    User user = new User();
    user.setId(1L);
    user.setUsername("john");

    AdoptionAddRequestDTO request = new AdoptionAddRequestDTO(
            pet,
            user.getId(),
            List.of("image1.jpg", "image2.jpg"),
            "123456789"
    );

    when(petRepository.findById(1)).thenReturn(Optional.empty());
    when(petRepository.save(any(Pet.class))).thenReturn(pet);
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(adoptionRepository.save(any(AdoptionEntry.class))).thenAnswer(invocation -> invocation.getArgument(0));

    AdoptionEntry result = adoptionService.createAdoption(request);

    assertNotNull(result);
    assertEquals(user, result.getPublisher());

    verify(petRepository, times(2)).save(any(Pet.class));
    verify(adoptionRepository).save(any(AdoptionEntry.class));
}

    @Test
    void createAdoption_whenPetExistsAndUserAlreadyRequested_shouldThrowException() {
        Pet pet = new Pet();
        pet.setId(2);

        User user = new User();
        user.setId(1L);
        user.setUsername("alice");

        AdoptionAddRequestDTO request = new AdoptionAddRequestDTO(
                pet,
                user.getId(),
                List.of("image1.jpg", "image2.jpg"),
                "123456789"
        );

        when(petRepository.findById(2)).thenReturn(Optional.of(pet));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(adoptionRepository.existsByPetIdAndPublisherIdAndAdopterIsNull(2, 1L))
                .thenReturn(true);

        assertThrows(RuntimeException.class, () -> adoptionService.createAdoption(request));
    }

    @Test
    void createAdoption_whenUserNotFound_shouldThrowException() {
        Pet pet = new Pet();
        pet.setId(3);

        User user = new User();
        user.setId(999L);
        user.setUsername("ghost");

        AdoptionAddRequestDTO request = new AdoptionAddRequestDTO(
                pet,
                user.getId(),
                List.of("image1.jpg", "image2.jpg"),
                "123456789"
        );

        when(petRepository.findById(3)).thenReturn(Optional.of(pet));
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> adoptionService.createAdoption(request));
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