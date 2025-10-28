package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.model.AdoptionRequest;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.model.UserModel;
import cloudflight.integra.backend.dto.AdoptionAddRequestDTO;
import cloudflight.integra.backend.mapper.AdoptionMapper;
import cloudflight.integra.backend.model.AdoptionEntry;
import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.model.User;
import cloudflight.integra.backend.repository.AdoptionRepository;
import cloudflight.integra.backend.repository.PetRepository;
import cloudflight.integra.backend.repository.UserRepository;
import cloudflight.integra.backend.service.AdoptionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdoptionServiceImpl implements AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public AdoptionServiceImpl(AdoptionRepository adoptionRepository, PetRepository petRepository, UserRepository userRepository) {
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

    @Override
    public List<AdoptionRequest> getAllRequests() {
        return adoptionRepository.findAll();
    }

    @Override
    public AdoptionRequest createRequest(Long petId, Long userId) {
        return null;
    }

    @Override
    public AdoptionRequest createRequest(Long petId, String username) {
        Pet pet = petRepository.findById(Math.toIntExact(petId))
                .orElseThrow(() -> new RuntimeException("Pet not found"));
        UserModel user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AdoptionRequest request = AdoptionRequest.builder()
                .pet(pet)
                .user(user)
                .status("PENDING")
                .build();

        return adoptionRepository.save(request);
    }

    @Override
    public AdoptionRequest approveRequest(Long id) {
        AdoptionRequest req = adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        req.setStatus("APPROVED");
        return adoptionRepository.save(req);
    }

    @Override
    public AdoptionRequest rejectRequest(Long id) {
        AdoptionRequest req = adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        req.setStatus("REJECTED");
        return adoptionRepository.save(req);
    }
    /**
     * Adds an adoption listing , if the pet contained doesn't exist it will be added as well
     * @param adoptionAddRequest the listing to be added
     * @return the listing added
     * @throws RuntimeException if the user contained does not exist
     */
    @Override
    public AdoptionEntry createAdoption(AdoptionAddRequestDTO adoptionAddRequest) {
        Pet toBePublished = adoptionAddRequest.pet();

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

        User publisher = userRepository.findById(adoptionAddRequest.publisherId()).orElseThrow(() -> new RuntimeException("User not found"));

        AdoptionEntry newEntry = AdoptionMapper.INSTANCE.toModelFromAddRequest(adoptionAddRequest);

        newEntry.setPublisher(publisher);
        newEntry.setPet(toBePublished);

        return adoptionRepository.save(newEntry);
    }

    @Override
    public List<AdoptionEntry> getPendingAdoptions() {
        return adoptionRepository.findByAdopterIsNull();
    }


}
