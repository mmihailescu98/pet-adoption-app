package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.AdoptionRequest;
import java.util.List;

public interface AdoptionService {
    List<AdoptionRequest> getAllRequests();
    AdoptionRequest createRequest(Long petId, Long userId);

    AdoptionRequest createRequest(Long petId, String username);

    AdoptionRequest approveRequest(Long id);
    AdoptionRequest rejectRequest(Long id);
}
