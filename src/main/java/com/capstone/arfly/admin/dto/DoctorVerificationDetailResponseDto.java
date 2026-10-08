package com.capstone.arfly.admin.dto;

import com.capstone.arfly.admin.domain.DoctorVerification;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "[관리자] 의사 인증 신청 상세 응답")
public class DoctorVerificationDetailResponseDto {

    @Schema(description = "인증 신청 ID", example = "1")
    private Long verificationId;

    @Schema(description = "신청자 닉네임", example = "김수의")
    private String nickName;

    @Schema(description = "신청자 전화번호 (소셜 가입 회원은 null일 수 있음)", example = "010-1111-2222")
    private String phoneNumber;

    @Schema(description = "신청 일시", example = "2026-10-08T14:30:00")
    private LocalDateTime createdAt;

    @Schema(
            description = "면허증 사진 URL (10분간 유효한 서명 URL)",
            example = "https://bucket.s3.ap-northeast-2.amazonaws.com/...")
    private String licenseImageUrl;

    public static DoctorVerificationDetailResponseDto of(DoctorVerification verification, String licenseImageUrl) {
        return DoctorVerificationDetailResponseDto.builder()
                .verificationId(verification.getId())
                .nickName(verification.getMember().getNickName())
                .phoneNumber(verification.getMember().getPhoneNumber())
                .createdAt(verification.getCreatedAt())
                .licenseImageUrl(licenseImageUrl)
                .build();
    }
}
