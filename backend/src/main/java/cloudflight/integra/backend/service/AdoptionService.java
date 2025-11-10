package cloudflight.integra.backend.service;

import cloudflight.integra.backend.dto.AdoptionAddRequestDTO;
import cloudflight.integra.backend.model.AdoptionEntry;

import java.util.List;

public interface AdoptionService {
    AdoptionEntry createAdoption(AdoptionAddRequestDTO adoptionEntry);
    List<AdoptionEntry> getPendingAdoptions();
    boolean hasUserRequestedAdoption(Integer petId, Long userId);
}
