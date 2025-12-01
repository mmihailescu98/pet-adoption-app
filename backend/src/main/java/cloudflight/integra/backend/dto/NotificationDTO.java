package cloudflight.integra.backend.dto;

import java.time.LocalDateTime;

public record NotificationDTO(
        Long id,
        String title,
        String message,
        LocalDateTime timestamp,
        boolean isRead,
        Integer petId,
        String petName,
        String petSpecies
) {}
