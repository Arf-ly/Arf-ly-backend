package com.capstone.arfly.jetson.controller;

import com.capstone.arfly.jetson.dto.JetsonDeviceRegisterRequestDto;
import com.capstone.arfly.jetson.dto.JetsonDeviceRegisterResponseDto;
import com.capstone.arfly.jetson.service.JetsonDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Jetson", description = "젯슨 나노 기기 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/jetson")
public class JetsonDeviceController {
    private final JetsonDeviceService jetsonDeviceService;

    @Operation(summary = "젯슨 기기 등록", description = "로그인한 회원의 젯슨 기기를 등록하고 기기 토큰을 발급합니다. 토큰은 이 응답에서만 확인할 수 있습니다.")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "등록 성공"),
                @ApiResponse(responseCode = "400", description = "기기 이름 누락 또는 50자 초과"),
                @ApiResponse(responseCode = "401", description = "인증 실패 (토큰 만료 혹은 유효하지 않은 토큰)"),
                @ApiResponse(responseCode = "404", description = "존재하지 않는 회원")
            })
    @PostMapping
    public ResponseEntity<JetsonDeviceRegisterResponseDto> register(
            @Valid @RequestBody JetsonDeviceRegisterRequestDto request,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetails userDetails) {
        long memberId = Long.parseLong(userDetails.getUsername());
        return new ResponseEntity<>(jetsonDeviceService.register(memberId, request.getName()), HttpStatus.CREATED);
    }
}
