package cloudflight.integra.backend.dto;

import cloudflight.integra.backend.model.StatsType;

public record AdoptionStatsDTO(StatsType type, String name, long adoptedCount) {
}
