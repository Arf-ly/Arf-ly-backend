package com.capstone.arfly.admin.dto;

import com.capstone.arfly.admin.domain.Report;
import com.capstone.arfly.admin.domain.ReportReason;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "[관리자] 신고 목록 항목")
public class ReportListResponseDto {

    @Schema(description = "신고 ID (상세 조회 시 사용)", example = "1")
    private Long reportId;

    @Schema(description = "신고된 게시글 제목", example = "광고 글입니다")
    private String postTitle;

    @Schema(description = "신고 사유 (SPAM, ABUSE, OBSCENE, ADVERTISEMENT, ETC)", example = "ADVERTISEMENT")
    private ReportReason reason;

    @Schema(description = "신고자 닉네임", example = "유저123")
    private String reporterNickName;

    @Schema(description = "신고 일시", example = "2026-10-10T14:30:00")
    private LocalDateTime createdAt;

    public static ReportListResponseDto from(Report report) {
        return ReportListResponseDto.builder()
                .reportId(report.getId())
                .postTitle(report.getPost().getTitle())
                .reason(report.getReason())
                .reporterNickName(report.getReporter().getNickName())
                .createdAt(report.getCreatedAt())
                .build();
    }
}
