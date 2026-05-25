package com.capstone.arfly.walkRecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "주간 산책 거리 리포트 응답")
public class WeeklyDistanceResponse {

    @Schema(description = "반려동물 기본 정보")
    private PetReportDto pet;

    @Schema(
            description = "요일별(MON~SUN) 이동 거리(km). 기록 없는 요일은 0으로 채워 7일치 항상 반환",
            example = """
                    {
                      "MON": {"distanceKm": 3.2},
                      "TUE": {"distanceKm": 4.1},
                      "WED": {"distanceKm": 0.0},
                      "THU": {"distanceKm": 5.0},
                      "FRI": {"distanceKm": 3.5},
                      "SAT": {"distanceKm": 1.9},
                      "SUN": {"distanceKm": 2.2}
                    }"""
    )
    private Map<String, DayDistanceDto> week;

    @Schema(description = "주간 평균 이동 거리 (km)", example = "3.24")
    private double averageDistanceKm;
}
