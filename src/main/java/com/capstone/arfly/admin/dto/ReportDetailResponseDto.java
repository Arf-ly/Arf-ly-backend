package com.capstone.arfly.admin.dto;

import com.capstone.arfly.admin.domain.Report;
import com.capstone.arfly.admin.domain.ReportReason;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "[관리자] 신고 상세 응답")
public class ReportDetailResponseDto {

    @Schema(description = "신고 ID", example = "1")
    private Long reportId;

    @Schema(description = "신고된 게시글 ID (게시글 상세 페이지 링크용)", example = "10")
    private Long postId;

    @Schema(description = "신고 사유 (SPAM, ABUSE, OBSCENE, ADVERTISEMENT, ETC)", example = "ETC")
    private ReportReason reason;

    @Schema(description = "상세 사유 (입력하지 않았으면 null)", example = "다른 회원을 비방하는 내용이 포함되어 있습니다.")
    private String detail;

    @Schema(description = "신고자 닉네임", example = "유저123")
    private String reporterNickName;

    @Schema(description = "게시글 작성자 ID (회원 관리용)", example = "5")
    private Long authorId;

    @Schema(description = "게시글 작성자 닉네임", example = "광고계정")
    private String authorNickName;

    @Schema(description = "신고 일시", example = "2026-10-10T14:30:00")
    private LocalDateTime createdAt;

    public static ReportDetailResponseDto from(Report report) {
        return ReportDetailResponseDto.builder()
                .reportId(report.getId())
                .postId(report.getPost().getId())
                .reason(report.getReason())
                .detail(report.getDetail())
                .reporterNickName(report.getReporter().getNickName())
                .authorId(report.getPost().getMember().getId())
                .authorNickName(report.getPost().getMember().getNickName())
                .createdAt(report.getCreatedAt())
                .build();
    }
}
