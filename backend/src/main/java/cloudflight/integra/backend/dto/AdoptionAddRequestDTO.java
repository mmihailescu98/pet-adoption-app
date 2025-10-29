package cloudflight.integra.backend.dto;

import java.util.List;

public record AdoptionAddRequestDTO(
        PetDTO pet,
        Long publisherId,
        List<String> additionalImages,
        String contactNumber
)
{}
