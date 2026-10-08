package com.capstone.arfly.admin.controller;

import com.capstone.arfly.admin.service.DoctorVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/doctor-verifications")
public class DoctorVerificationController {
    private final DoctorVerificationService doctorVerificationService;

    @Operation(summary = "의사 인증 신청", description = "면허증 사진 1장을 업로드하여 의사 인증을 신청합니다. 관리자 승인 후 DOCTOR 권한이 부여됩니다.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "인증 신청 성공"),
                @ApiResponse(responseCode = "400", description = "파일 누락 또는 이미지가 아닌 파일"),
                @ApiResponse(responseCode = "401", description = "인증 실패 (토큰 만료 혹은 유효하지 않은 토큰)"),
                @ApiResponse(responseCode = "409", description = "이미 의사 회원이거나 심사 대기 중인 신청이 있음"),
                @ApiResponse(responseCode = "500", description = "서버 ERROR(EX.S3 Upload 과정 중 오류 발생)")
            })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> apply(
            @RequestPart("file") MultipartFile file,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        long memberId = Long.parseLong(userDetails.getUsername());
        doctorVerificationService.apply(memberId, file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
