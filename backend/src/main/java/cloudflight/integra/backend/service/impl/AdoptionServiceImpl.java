package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.dto.AdoptionAddRequestDTO;
import cloudflight.integra.backend.listener.PetAddedEvent;
import cloudflight.integra.backend.mapper.AdoptionMapper;
import cloudflight.integra.backend.mapper.PetMapper;
import cloudflight.integra.backend.model.AdoptionEntry;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.model.PetStatus;
import cloudflight.integra.backend.model.User;
import cloudflight.integra.backend.repository.AdoptionRepository;
import cloudflight.integra.backend.repository.PetRepository;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.AdoptionService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptionServiceImpl implements AdoptionService {
    AdoptionRepository adoptionRepository;
    PetRepository petRepository;
    UserRepository userRepository;
    ApplicationEventPublisher eventPublisher;

    AdoptionServiceImpl(
            AdoptionRepository adoptionRepository,
            PetRepository petRepository,
            UserRepository userRepository,
            ApplicationEventPublisher eventPublisher
    ) {
        this.adoptionRepository = adoptionRepository;
        this.petRepository = petRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * Adds an adoption listing , if the pet contained doesn't exist it will be added as well
     * @param adoptionAddRequest the listing to be added
     * @return the listing added
     * @throws RuntimeException if the user contained does not exist
     */
    @Override
    public AdoptionEntry createAdoption(AdoptionAddRequestDTO adoptionAddRequest) {
        Pet toBePublished = PetMapper.INSTANCE.petDTOToPet(adoptionAddRequest.pet());

        boolean existingPet = false;
        boolean newPetCreated = false;
        try {
            toBePublished = petRepository.findById(toBePublished.getId()).orElseThrow();
            existingPet = true;
        }catch (Exception _){
            toBePublished = petRepository.save(toBePublished);
            newPetCreated = true;
        }

        User publisher = userRepository.findById(adoptionAddRequest.publisherId()).orElseThrow(() -> new RuntimeException("User not found"));

        final Pet finalToBePublished = toBePublished;

        if (existingPet) {
            boolean userAlreadyRequested = adoptionRepository
                    .existsByPetIdAndPublisherIdAndAdopterIsNull(toBePublished.getId(), publisher.getId());

            if (userAlreadyRequested) {
                throw new RuntimeException("You have already requested adoption for this pet");
            }
        }

        AdoptionEntry newEntry = AdoptionMapper.INSTANCE.toModelFromAddRequest(adoptionAddRequest);

        newEntry.setPublisher(publisher);
        newEntry.setPet(toBePublished);

        toBePublished.setStatus(PetStatus.PENDING);
        petRepository.save(toBePublished);

        AdoptionEntry savedEntry = adoptionRepository.save(newEntry);

        if (newPetCreated) {
            eventPublisher.publishEvent(new PetAddedEvent(toBePublished.getId(), publisher.getId()));
        }

        return savedEntry;
    }

    @Override
    public List<AdoptionEntry> getPendingAdoptions() {
        return adoptionRepository.findByAdopterIsNull();
    }

    @Override
    public boolean hasUserRequestedAdoption(Integer petId, Long userId) {
        return adoptionRepository.existsByPetIdAndPublisherIdAndAdopterIsNull(petId, userId);
    }

}
