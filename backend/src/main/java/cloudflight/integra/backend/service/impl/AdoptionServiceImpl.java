package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.model.AdoptionEntry;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.model.UserModel;
import cloudflight.integra.backend.repository.AdoptionRepository;
import cloudflight.integra.backend.repository.PetRepository;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.AdoptionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptionServiceImpl implements AdoptionService {
    AdoptionRepository adoptionRepository;
    PetRepository petRepository;
    UserRepository userRepository;

    AdoptionServiceImpl(
            AdoptionRepository adoptionRepository,
            PetRepository petRepository,
            UserRepository userRepository
    ) {
        this.adoptionRepository = adoptionRepository;
        this.petRepository = petRepository;
        this.userRepository = userRepository;
    }

    /**
     * Adds an adoption listing , if the pet contained doesn't exist it will be added as well
     * @param adoptionEntry the listing to be added
     * @return the listing added
     * @throws RuntimeException if the user contained does not exist
     */
    @Override
    public AdoptionEntry createAdoption(AdoptionEntry adoptionEntry) {
        Pet toBePublished = adoptionEntry.getPet();
        UserModel publisher = adoptionEntry.getPublisher();

        boolean existingPet = false;

        try {
            toBePublished = petRepository.findById(toBePublished.getId()).orElseThrow();
            existingPet = true;
        }catch (Exception _){
            toBePublished = petRepository.save(toBePublished);
        }

        if (existingPet) {
            //if the pet exists , we check for it to not be in an already pending adoption
            adoptionRepository.findByPetAndAdopterIsNull(toBePublished)
                    .ifPresent((_)->{
                        throw(new RuntimeException("This pet is already in a pending adoption"));
                    });
        }

        publisher = userRepository.findByUsername(publisher.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));

        adoptionEntry.setPublisher(publisher);
        adoptionEntry.setPet(toBePublished);

        return adoptionRepository.save(adoptionEntry);
    }

    @Override
    public List<AdoptionEntry> getPendingAdoptions() {
        return adoptionRepository.findByAdopterIsNull();
    }


}
