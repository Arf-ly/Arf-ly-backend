package com.capstone.arfly.walkRecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "배정된 산책 기록 단건")
public class AssignedWalkDto {

    @Schema(description = "산책 기록 ID", example = "4")
    private Long id;

    @Schema(description = "산책 날짜 (년/월/일/요일)")
    private WalkDateDto date;

    @Schema(description = "산책 시작 시각 (HH:mm)", example = "09:00")
    private String startTime;

    @Schema(description = "산책 종료 시각 (HH:mm)", example = "09:50")
    private String endTime;

    @Schema(description = "총 산책 시간 (분)", example = "50")
    private long durationMinutes;

    @Schema(description = "총 이동 거리 (km)", example = "4.1")
    private double distanceKm;

    @Schema(description = "산책 점수 (activityScore)", example = "91.0")
    private double score;

    @Schema(description = "배정된 반려동물 정보")
    private WalkPetDto pet;
}
