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

    @Operation(summary = "기기 등록", description = "최초 설정 단계에서 발급된 멤버 ID와 기기를 매핑합니다.")
    @PostMapping("/register")
    public ResponseEntity<IoTResponseDto.RegisterResult> register(@RequestBody IoTRequestDto.Register request) {
        // 💡 핵심: iotService를 호출해서 진짜 deviceUid를 발급받아옵니다!
        String deviceUid = iotService.registerDevice(request);

        // 💡 핵심: 발급받은 deviceUid를 피코에게 JSON으로 돌려줍니다!
        return ResponseEntity.ok(new IoTResponseDto.RegisterResult(deviceUid));
    }

    @Operation(summary = "산책 데이터 일괄 업로드", description = "피코가 바디(JSON) 안에 deviceUid와 데이터를 함께 담아 보냅니다.")
    @PostMapping("/walks/upload")
    public ResponseEntity<Void> uploadWalks(
            // 💡 핵심: @RequestHeader 부분을 아예 삭제했습니다!
            @RequestBody IoTRequestDto.UploadWalk request) {

        // 서비스에게 request 상자를 통째로 던져줍니다.
        iotService.uploadWalkData(request);
        return ResponseEntity.ok().build();
    }
}