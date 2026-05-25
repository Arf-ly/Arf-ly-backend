package com.capstone.arfly.walkRecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "요일별 액티비티 스코어")
public class DayActivityDto {

    @Schema(description = "해당 요일의 액티비티 스코어 (기록 없으면 0)", example = "72.0")
    private double activityScore;
}
