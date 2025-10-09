package cloudflight.integra.backend.dto;

import cloudflight.integra.backend.model.User;

import java.util.List;

public record AdoptionListItemDTO(
        Long id,
        PetDTO pet,
        User publisher,
        List<String> additionalImages,
        String contactNumber
)
{}
