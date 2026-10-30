package com.capstone.arfly.admin.dto;

import com.capstone.arfly.admin.domain.ReportReason;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "게시글 신고 요청")
public class ReportCreateRequestDto {

    @Schema(
            description = "신고 사유 (SPAM: 스팸/도배, ABUSE: 욕설/비방, OBSCENE: 음란물, ADVERTISEMENT: 광고/홍보, ETC: 기타)",
            example = "ABUSE",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "신고 사유를 선택해주세요.") private ReportReason reason;

    @Schema(description = "상세 사유 (ETC일 때 필수, 최대 500자)", example = "다른 회원을 비방하는 내용이 포함되어 있습니다.")
    @Size(max = 500, message = "상세 사유는 500자 이하로 입력해주세요.") private String detail;
}
