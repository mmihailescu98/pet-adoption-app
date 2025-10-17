package cloudflight.integra.backend.service.impl;

import cloudflight.integra.backend.model.AdoptionRequest;
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

    private final AdoptionRepository adoptionRepository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;

    public AdoptionServiceImpl(AdoptionRepository adoptionRepository, PetRepository petRepository, UserRepository userRepository) {
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
}
