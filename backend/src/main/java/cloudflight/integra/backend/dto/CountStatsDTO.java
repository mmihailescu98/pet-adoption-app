package cloudflight.integra.backend.dto;

import cloudflight.integra.backend.model.StatsType;

public record CountStatsDTO(StatsType type, String name, long count) {
}
