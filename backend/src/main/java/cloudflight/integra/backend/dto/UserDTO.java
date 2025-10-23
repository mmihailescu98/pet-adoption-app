package cloudflight.integra.backend.dto;

public record UserDTO(
   Long id,
   String username,
   Boolean isAdmin
) {}
