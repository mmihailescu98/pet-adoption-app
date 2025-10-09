package cloudflight.integra.backend.dto;

import cloudflight.integra.backend.model.Pet;

import java.util.List;

public record AdoptionAddRequestDTO(
        Pet pet,
        Long publisherId,
        List<String> additionalImages,
        String contactNumber
)
{}
