package com.capstone.arfly.iot.controller;

import com.capstone.arfly.iot.dto.IoTRequestDto;
import com.capstone.arfly.iot.dto.IoTResponseDto;
import com.capstone.arfly.iot.service.IoTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "IoT", description = "IoT 기기 통신 및 센서 처리 API")
@RestController
@RequestMapping("/api/iot")
@RequiredArgsConstructor
public class IoTController {

    private final IoTService iotService;

    @Operation(summary = "기기 등록", description = "피코의 UID를 받아 회원과 매핑합니다.")
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody IoTRequestDto.Register request) {
        // 프론트엔드가 보낸 memberId와 deviceUid를 서비스로 전달
        iotService.registerDevice(request.getMemberId(), request.getDeviceUid());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "산책 데이터 일괄 업로드", description = "피코가 바디(JSON) 안에 deviceUid와 데이터를 함께 담아 보냅니다.")
    @PostMapping("/walks/upload")
    public ResponseEntity<Void> uploadWalks(@RequestBody IoTRequestDto.UploadWalk request) {
        iotService.uploadWalkData(request);
        return ResponseEntity.ok().build();
    }
}