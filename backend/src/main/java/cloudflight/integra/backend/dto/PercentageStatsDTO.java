package cloudflight.integra.backend.dto;

import cloudflight.integra.backend.model.StatsType;

public record PercentageStatsDTO(StatsType type, String name, double percentage) {
}
