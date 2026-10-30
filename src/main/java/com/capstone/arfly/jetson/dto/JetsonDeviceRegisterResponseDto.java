package com.capstone.arfly.jetson.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "젯슨 기기 등록 응답 DTO")
public class JetsonDeviceRegisterResponseDto {

    @Schema(description = "기기 ID", example = "1")
    private Long deviceId;

    @Schema(description = "기기 토큰 (이 응답에서만 확인 가능하므로 젯슨 설정 파일에 저장해야 함)", example = "q3Zt...")
    private String token;
}
