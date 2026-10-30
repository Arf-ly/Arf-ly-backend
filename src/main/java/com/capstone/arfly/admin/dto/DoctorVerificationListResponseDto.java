package com.capstone.arfly.admin.dto;

import com.capstone.arfly.admin.domain.DoctorVerification;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "[관리자] 의사 인증 신청 목록 항목")
public class DoctorVerificationListResponseDto {

    @Schema(description = "인증 신청 ID (상세 조회 시 사용)", example = "1")
    private Long verificationId;

    @Schema(description = "신청자 닉네임", example = "김수의")
    private String nickName;

    @Schema(description = "신청 일시", example = "2026-10-08T14:30:00")
    private LocalDateTime createdAt;

    public static DoctorVerificationListResponseDto from(DoctorVerification verification) {
        return DoctorVerificationListResponseDto.builder()
                .verificationId(verification.getId())
                .nickName(verification.getMember().getNickName())
                .createdAt(verification.getCreatedAt())
                .build();
    }
}
