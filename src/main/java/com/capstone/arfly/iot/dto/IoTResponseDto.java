package com.capstone.arfly.iot.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class IoTResponseDto {

    @Getter
    @AllArgsConstructor
    @Schema(description = "기기 등록 성공 응답")
    public static class RegisterResult {
        @Schema(description = "발급된 기기 UID (이후 통신 시 헤더에 사용)")
        private String deviceUid;
    }
}