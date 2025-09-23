package cloudflight.integra.backend.dto;

import cloudflight.integra.backend.model.Pet;
import cloudflight.integra.backend.model.UserModel;

import java.util.List;

public record AdoptionAddRequestDTO(
        Pet pet,
        UserModel publisher,
        List<String> additionalImages,
        String contactNumber
)
{}
