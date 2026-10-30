package com.capstone.arfly.jetson.controller;

import com.capstone.arfly.jetson.dto.JetsonAlertCreateRequestDto;
import com.capstone.arfly.jetson.service.JetsonAlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

// 젯슨 기기가 호출하는 API (사용자 JWT가 아닌 기기 토큰으로 인증)
@Tag(name = "Jetson", description = "젯슨 나노 기기 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jetson-device")
public class JetsonAlertController {
    private final JetsonAlertService jetsonAlertService;

    @Operation(
            summary = "[젯슨] 이상 징후 업로드",
            description = "젯슨이 추론한 피부 이상 징후(질환명, 확률)와 촬영 사진을 업로드합니다. Authorization 헤더에 기기 토큰이 필요합니다.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "업로드 성공"),
                @ApiResponse(responseCode = "400", description = "요청 값 누락, 확률 범위 오류 또는 이미지가 아닌 파일"),
                @ApiResponse(responseCode = "401", description = "유효하지 않은 기기 토큰"),
                @ApiResponse(responseCode = "500", description = "서버 ERROR(EX.S3 Upload 과정 중 오류 발생)")
            })
    @PostMapping(path = "/alerts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> create(
            @Valid @RequestPart("request") JetsonAlertCreateRequestDto request,
            @RequestPart("file") MultipartFile file,
            @Parameter(hidden = true) @AuthenticationPrincipal Long deviceId) {
        jetsonAlertService.create(deviceId, request, file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
