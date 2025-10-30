package cloudflight.integra.backend.dto;

import cloudflight.integra.backend.model.PetStatus;

public record PetDTO(
        Integer id,
        String species,
        String breed,
        String name,
        String location,
        String age,
        String description,
        String imgURL,
        Boolean isUserFavorite,
        PetStatus status
) {}

