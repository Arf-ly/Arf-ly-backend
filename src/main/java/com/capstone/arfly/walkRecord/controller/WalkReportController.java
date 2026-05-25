package com.capstone.arfly.walkRecord.controller;

import com.capstone.arfly.common.exception.ErrorResponse;
import com.capstone.arfly.walkRecord.dto.WeeklyDistanceResponse;
import com.capstone.arfly.walkRecord.dto.WeeklyStepsResponse;
import com.capstone.arfly.walkRecord.service.WalkRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "walk-report", description = "주간 활동 리포트 API")
@RestController
@RequestMapping("/api/pets/{petId}/report")
@RequiredArgsConstructor
public class WalkReportController {

    private final WalkRecordService walkRecordService;

    @Operation(summary = "주간 활동 리포트 — 걸음 수",
            description = """
                    date 기준 해당 주(월~일)의 요일별 액티비티 스코어와 주간 평균값을 반환합니다.
                    기록이 없는 요일은 activityScore: 0 으로 채워 7일치 항상 반환합니다.
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = WeeklyStepsResponse.class))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음 (내 반려동물이 아님)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "반려동물을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/steps")
    public ResponseEntity<WeeklyStepsResponse> getWeeklySteps(
            @Parameter(description = "반려동물 ID", required = true) @PathVariable Long petId,
            @Parameter(description = "해당 주 임의의 날짜 (YYYY-MM-DD)", example = "2026-05-19", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {

        Long memberId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(walkRecordService.getWeeklyStepsReport(memberId, petId, date));
    }

    @Operation(summary = "주간 활동 리포트 — 산책 거리",
            description = """
                    date 기준 해당 주(월~일)의 요일별 이동 거리(km)와 주간 평균값을 반환합니다.
                    기록이 없는 요일은 distanceKm: 0 으로 채워 7일치 항상 반환합니다.
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공",
                    content = @Content(schema = @Schema(implementation = WeeklyDistanceResponse.class))),
            @ApiResponse(responseCode = "403", description = "접근 권한 없음 (내 반려동물이 아님)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "반려동물을 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/distance")
    public ResponseEntity<WeeklyDistanceResponse> getWeeklyDistance(
            @Parameter(description = "반려동물 ID", required = true) @PathVariable Long petId,
            @Parameter(description = "해당 주 임의의 날짜 (YYYY-MM-DD)", example = "2026-05-19", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {

        Long memberId = Long.parseLong(userDetails.getUsername());
        return ResponseEntity.ok(walkRecordService.getWeeklyDistanceReport(memberId, petId, date));
    }
}
