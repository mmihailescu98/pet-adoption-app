package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.AdoptionEntry;

public interface AdoptionService {
    AdoptionEntry createAdoption(AdoptionEntry adoptionEntry);
}
