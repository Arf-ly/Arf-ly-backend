package com.capstone.arfly.admin.controller;

import com.capstone.arfly.admin.dto.DoctorVerificationListResponseDto;
import com.capstone.arfly.admin.service.DoctorVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/verifications")
public class AdminDoctorVerificationController {
    private final DoctorVerificationService doctorVerificationService;

    @Operation(summary = "[관리자] 의사 인증 신청 목록 조회", description = "심사 대기 중인 의사 인증 신청 목록을 오래된 순으로 조회합니다. 관리자만 접근할 수 있습니다.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "조회 성공 (신청이 없으면 빈 배열)"),
                @ApiResponse(responseCode = "401", description = "인증 실패 (토큰 만료 혹은 유효하지 않은 토큰)"),
                @ApiResponse(responseCode = "403", description = "관리자 권한 없음")
            })
    @GetMapping
    public ResponseEntity<List<DoctorVerificationListResponseDto>> getPendingVerifications() {
        return ResponseEntity.ok(doctorVerificationService.getPendingVerifications());
    }
}
