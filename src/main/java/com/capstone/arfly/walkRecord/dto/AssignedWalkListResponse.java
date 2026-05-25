package com.capstone.arfly.walkRecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(description = "배정된 산책 기록 목록 응답")
public class AssignedWalkListResponse {

    @Schema(description = "배정된 산책 기록 목록")
    private List<AssignedWalkDto> walks;
}
