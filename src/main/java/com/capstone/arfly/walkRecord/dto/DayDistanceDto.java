package com.capstone.arfly.walkRecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "요일별 이동 거리")
public class DayDistanceDto {

    @Schema(description = "해당 요일의 총 이동 거리 (km, 기록 없으면 0)", example = "3.2")
    private double distanceKm;
}
