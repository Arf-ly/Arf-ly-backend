package com.capstone.arfly.jetson.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "젯슨 이상 징후 업로드 요청 DTO")
public class JetsonAlertCreateRequestDto {

    @Schema(description = "의심 질환명", example = "구진, 플라크", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "질환명은 필수 항목입니다.") private String diseaseName;

    @Schema(description = "확률(%)", example = "87.5", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "확률은 필수 항목입니다.") @DecimalMin(value = "0.0", message = "확률은 0 이상이어야 합니다.") @DecimalMax(value = "100.0", message = "확률은 100 이하여야 합니다.") private Double probability;
}
