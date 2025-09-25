package cloudflight.integra.backend.service;

import cloudflight.integra.backend.model.AdoptionEntry;

import java.util.List;

public interface AdoptionService {
    AdoptionEntry createAdoption(AdoptionEntry adoptionEntry);
    List<AdoptionEntry> getPendingAdoptions();
}
