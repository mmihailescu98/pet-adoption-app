package cloudflight.integra.backend.dto;

public record PetDTO(
        Integer id,
        String species,
        String breed,
        String name,
        String location,
        String age,
        String description,
        String imgURL
) {}

