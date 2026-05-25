package com.capstone.arfly.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "파이어베이스 토큰 DTO")
public class FirebaseToken {

    @Schema(
            description = "파이어베이스 토큰 ",
            example = "AJDJDHAKQUDNIDBDJQDUDNDIDBDKDJ",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    @NotBlank(message = "Firebase Token은 필수입니다!")
    private String token;

}