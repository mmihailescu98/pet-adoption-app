package cloudflight.integra.backend.dto;

import cloudflight.integra.backend.model.PetStatus;

import java.time.LocalDateTime;

public record AdoptedPetDTO(
        Integer id,
        String species,
        String breed,
        String name,
        LocationDTO location,
        String age,
        String description,
        String imgURL,
        PetStatus status,
        LocalDateTime createdAt,
        LocalDateTime adoptedAt
) {}
