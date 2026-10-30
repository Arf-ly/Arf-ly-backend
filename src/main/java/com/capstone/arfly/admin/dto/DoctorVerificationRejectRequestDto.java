package com.capstone.arfly.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "[관리자] 의사 인증 반려 요청")
public class DoctorVerificationRejectRequestDto {

    @Schema(
            description = "반려 사유 (최대 255자)",
            example = "면허증 사진이 흐릿하여 확인이 어렵습니다.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "반려 사유를 입력해주세요.") @Size(max = 255, message = "반려 사유는 255자 이하로 입력해주세요.") private String reason;
}
