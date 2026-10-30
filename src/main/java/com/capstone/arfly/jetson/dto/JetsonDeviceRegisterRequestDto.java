package com.capstone.arfly.jetson.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "젯슨 기기 등록 요청 DTO")
public class JetsonDeviceRegisterRequestDto {

    @Schema(description = "기기 이름", example = "거실 젯슨", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "기기 이름은 필수 항목입니다.") @Size(max = 50, message = "기기 이름은 50자 이하여야 합니다.") private String name;
}
