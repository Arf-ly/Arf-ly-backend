package com.capstone.arfly.walkRecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@Schema(description = "주간 액티비티 스코어(걸음 수) 리포트 응답")
public class WeeklyStepsResponse {

    @Schema(description = "반려동물 기본 정보")
    private PetReportDto pet;

    @Schema(
            description = "요일별(MON~SUN) 액티비티 스코어. 기록 없는 요일은 0으로 채워 7일치 항상 반환",
            example = """
                    {
                      "MON": {"activityScore": 72.0},
                      "TUE": {"activityScore": 88.0},
                      "WED": {"activityScore": 0.0},
                      "THU": {"activityScore": 91.0},
                      "FRI": {"activityScore": 78.0},
                      "SAT": {"activityScore": 55.0},
                      "SUN": {"activityScore": 60.0}
                    }"""
    )
    private Map<String, DayActivityDto> week;

    @Schema(description = "주간 평균 액티비티 스코어", example = "72.7")
    private double averageActivityScore;
}
