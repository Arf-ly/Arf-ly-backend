package com.capstone.arfly.walkRecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "미배정 산책 기록 단건")
public class UnassignedWalkDto {

    @Schema(description = "산책 기록 ID", example = "1")
    private Long id;

    @Schema(description = "산책 날짜 (년/월/일/요일)")
    private WalkDateDto date;

    @Schema(description = "산책 시작 시각 (HH:mm)", example = "08:30")
    private String startTime;

    @Schema(description = "산책 종료 시각 (HH:mm)", example = "09:15")
    private String endTime;

    @Schema(description = "총 산책 시간 (분)", example = "45")
    private long durationMinutes;

    @Schema(description = "총 이동 거리 (km)", example = "3.2")
    private double distanceKm;

    @Schema(description = "산책 점수 (activityScore)", example = "87.0")
    private double score;
}
