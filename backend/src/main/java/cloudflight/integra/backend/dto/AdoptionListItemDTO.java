package cloudflight.integra.backend.dto;

import java.util.List;

public record AdoptionListItemDTO(
        Long id,
        PetDTO pet,
        UserDTO publisher,
        List<String> additionalImages,
        String contactNumber
)
{}
