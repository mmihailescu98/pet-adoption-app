package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.dto.AdoptionAddRequestDTO;
import cloudflight.integra.backend.dto.PetDTO;
import cloudflight.integra.backend.listener.PetAddedEvent;
import cloudflight.integra.backend.mapper.PetMapper;
import cloudflight.integra.backend.model.AdoptionEntry;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.model.User;
import cloudflight.integra.backend.repository.AdoptionRepository;
import cloudflight.integra.backend.repository.PetRepository;
import cloudflight.integra.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

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

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private AdoptionServiceImpl adoptionService;

    @Test
    void createAdoption_whenPetDoesNotExist_shouldSavePetAndAdoption() {
        Pet pet = new Pet();
        pet.setId(1);

        User user = new User();
        user.setId(1L);
        user.setUsername("john");

        AdoptionEntry adoptionEntry = new AdoptionEntry();
        adoptionEntry.setPet(pet);
        adoptionEntry.setPublisher(user);

        PetDTO petDTO = PetMapper.INSTANCE.petToPetDTO(pet);
        AdoptionAddRequestDTO request = new AdoptionAddRequestDTO(
                petDTO,
                user.getId(),
                List.of("image1.jpg", "image2.jpg"),
                "123456789"
        );

        when(petRepository.findById(1)).thenReturn(Optional.empty());
        when(petRepository.save(any(Pet.class))).thenReturn(pet);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(adoptionRepository.save(any(AdoptionEntry.class))).thenReturn(adoptionEntry);

        AdoptionEntry result = adoptionService.createAdoption(request);

        assertEquals(request.pet().name(), result.getPet().getName());
        // AdoptionAddRequestDTO contains a PetDTO. The service maps it to a new Pet instance,
        // so we verify using any(Pet.class) instead of a specific object reference.
        verify(petRepository).save(any(Pet.class));
        verify(eventPublisher).publishEvent(any(PetAddedEvent.class));
    }

    @Test
    void createAdoption_whenPetExistsAndAlreadyPending_shouldThrowException() {
        Pet pet = new Pet();
        pet.setId(2);

        User user = new User();
        user.setUsername("alice");

        PetDTO petDTO = PetMapper.INSTANCE.petToPetDTO(pet);
        AdoptionAddRequestDTO request = new AdoptionAddRequestDTO(
                petDTO,
                user.getId(),
                List.of("image1.jpg", "image2.jpg"),
                "123456789"
        );

        when(petRepository.findById(2)).thenReturn(Optional.of(pet));
        when(adoptionRepository.findByPetAndAdopterIsNull(pet))
                .thenReturn(Optional.of(new AdoptionEntry()));

        assertThrows(RuntimeException.class, () -> adoptionService.createAdoption(request));
    }

    @Test
    void createAdoption_whenUserNotFound_shouldThrowException() {
        Pet pet = new Pet();
        pet.setId(3);

        User user = new User();
        user.setUsername("ghost");

        PetDTO petDTO = PetMapper.INSTANCE.petToPetDTO(pet);
        AdoptionAddRequestDTO request = new AdoptionAddRequestDTO(
                petDTO,
                user.getId(),
                List.of("image1.jpg", "image2.jpg"),
                "123456789"
        );

        when(petRepository.findById(3)).thenReturn(Optional.of(pet));
        when(adoptionRepository.findByPetAndAdopterIsNull(pet)).thenReturn(Optional.empty());
        when(userRepository.findById(any())).thenReturn(Optional.empty());

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
