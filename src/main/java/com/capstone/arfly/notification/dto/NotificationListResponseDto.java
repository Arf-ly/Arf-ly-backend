package com.capstone.arfly.notification.dto;

import com.capstone.arfly.notification.domain.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "알림 목록 조회 (무한 스크롤)")
public class NotificationListResponseDto {

    @Schema(description = "알림 목록 (최신순)")
    private List<NotificationSummary> notifications;

    @Schema(description = "페이지 메타 정보")
    private Meta meta;

    @Getter
    @Builder
    public static class NotificationSummary {
        @Schema(description = "알림 ID", example = "12")
        private Long id;

        @Schema(description = "알림 종류 (MENTION: 댓글 언급, JETSON_ALERT: 젯슨 이상 징후)", example = "JETSON_ALERT")
        private NotificationType type;

        @Schema(description = "클릭 시 이동할 대상 ID (MENTION: 게시글 ID, JETSON_ALERT: 이상 징후 ID)", example = "3")
        private Long referenceId;

        @Schema(description = "알림 제목", example = "피부 이상 징후 감지")
        private String title;

        @Schema(description = "알림 내용", example = "구진, 플라크 의심 (87.5%)")
        private String content;

        @Schema(
                description = "이상 징후 사진 URL (10분간 유효한 서명 URL, 언급 알림은 null)",
                example = "https://s3.../photo.jpg?X-Amz-...")
        private String imageUrl;

        @Schema(description = "알림 생성 일시", example = "2026-10-30T18:02:24")
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class Meta {
        @Schema(description = "다음 페이지 존재 여부", example = "true")
        private boolean hasNext;

        @Schema(description = "다음 스크롤 조회를 위한 커서 (마지막 알림 ID)", example = "12")
        private Long nextCursor;

        @Schema(description = "요청한 페이지 사이즈", example = "20")
        private Integer size;
    }
}
