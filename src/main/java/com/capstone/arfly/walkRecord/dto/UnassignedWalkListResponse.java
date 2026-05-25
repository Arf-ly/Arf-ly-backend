package com.capstone.arfly.walkRecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "미배정 산책 기록 목록 응답")
public class UnassignedWalkListResponse {

    @Schema(description = "미배정 산책 기록 총 개수", example = "3")
    private int totalCount;

    @Schema(description = "미배정 산책 기록 목록")
    private List<UnassignedWalkDto> walks;
}
