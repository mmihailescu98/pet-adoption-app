package cloudflight.integra.backend.dto;

public record LocationDTO(
        Integer id,
        String street,
        String city,
        String state,
        Double latitude,
        Double longitude
) {}
