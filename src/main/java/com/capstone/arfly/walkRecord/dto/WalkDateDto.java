package com.capstone.arfly.walkRecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "산책 날짜 정보")
public class WalkDateDto {

    @Schema(description = "연도", example = "2026")
    private int year;

    @Schema(description = "월 (1~12)", example = "5")
    private int month;

    @Schema(description = "일 (1~31)", example = "25")
    private int day;

    @Schema(description = "요일 (MON~SUN)", example = "MON")
    private String dayOfWeek;
}
