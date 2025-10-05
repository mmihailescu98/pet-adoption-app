package cloudflight.integra.backend.dto;

import java.util.Set;

public record UserDTO(Long id, String username, Set<String> roles, String name, String phone, String email, String location, String imgURL, String bio) {
}
