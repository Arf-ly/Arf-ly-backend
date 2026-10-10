package com.capstone.arfly.admin.controller;

import com.capstone.arfly.admin.dto.ReportDetailResponseDto;
import com.capstone.arfly.admin.dto.ReportListResponseDto;
import com.capstone.arfly.admin.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/reports")
public class AdminReportController {
    private final ReportService reportService;

    @Operation(summary = "[관리자] 신고 목록 조회", description = "처리 대기 중인 게시글 신고 목록을 오래된 순으로 조회합니다. 관리자만 접근할 수 있습니다.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "조회 성공 (신고가 없으면 빈 배열)"),
                @ApiResponse(responseCode = "401", description = "인증 실패 (토큰 만료 혹은 유효하지 않은 토큰)"),
                @ApiResponse(responseCode = "403", description = "관리자 권한 없음")
            })
    @GetMapping
    public ResponseEntity<List<ReportListResponseDto>> getPendingReports() {
        return ResponseEntity.ok(reportService.getPendingReports());
    }

    @Operation(
            summary = "[관리자] 신고 상세 조회",
            description = "신고 사유, 상세 사유, 신고자와 게시글 작성자 정보를 조회합니다. postId로 게시글 상세 페이지에 이동할 수 있습니다.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "조회 성공"),
                @ApiResponse(responseCode = "401", description = "인증 실패 (토큰 만료 혹은 유효하지 않은 토큰)"),
                @ApiResponse(responseCode = "403", description = "관리자 권한 없음"),
                @ApiResponse(responseCode = "404", description = "존재하지 않는 신고")
            })
    @GetMapping("/{reportId}")
    public ResponseEntity<ReportDetailResponseDto> getReportDetail(@PathVariable Long reportId) {
        return ResponseEntity.ok(reportService.getReportDetail(reportId));
    }
}
