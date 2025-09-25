package cloudflight.integra.backend.dto;

import cloudflight.integra.backend.model.UserModel;

import java.util.List;

public record AdoptionListItemDTO(
        Long id,
        PetDTO pet,
        UserModel publisher,
        List<String> additionalImages,
        String contactNumber
)
{}
