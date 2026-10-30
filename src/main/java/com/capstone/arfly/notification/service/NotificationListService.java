package com.capstone.arfly.notification.service;

import com.capstone.arfly.common.domain.File;
import com.capstone.arfly.common.util.S3Uploader;
import com.capstone.arfly.notification.domain.Notification;
import com.capstone.arfly.notification.dto.NotificationListResponseDto;
import com.capstone.arfly.notification.repository.NotificationRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationListService {
    private final NotificationRepository notificationRepository;
    private final S3Uploader s3Uploader;

    // 내 알림 목록 조회 (최신순, 커서 기반 무한 스크롤)
    @Transactional(readOnly = true)
    public NotificationListResponseDto getNotifications(Long memberId, Long cursor, int size) {
        // 다음 페이지 존재 여부 확인을 위해 1개 더 조회
        List<Notification> notifications = new ArrayList<>(
                notificationRepository.findNotificationsWithPaging(memberId, cursor, PageRequest.of(0, size + 1)));

        boolean hasNext = notifications.size() > size;
        if (hasNext) {
            notifications.remove(size);
        }

        Long nextCursor = notifications.isEmpty()
                ? null
                : notifications.get(notifications.size() - 1).getId();

        List<NotificationListResponseDto.NotificationSummary> summaries = notifications.stream()
                .map(notification -> NotificationListResponseDto.NotificationSummary.builder()
                        .id(notification.getId())
                        .type(notification.getType())
                        .referenceId(notification.getReferenceId())
                        .title(notification.getTitle())
                        .content(notification.getContent())
                        .imageUrl(getImageUrl(notification.getImage()))
                        .createdAt(notification.getCreatedAt())
                        .build())
                .toList();

        return NotificationListResponseDto.builder()
                .notifications(summaries)
                .meta(NotificationListResponseDto.Meta.builder()
                        .hasNext(hasNext)
                        .nextCursor(nextCursor)
                        .size(size)
                        .build())
                .build();
    }

    // 이상 징후 사진은 10분간 유효한 서명 URL로 제공 (언급 알림이나 삭제된 파일은 null)
    private String getImageUrl(File image) {
        if (image == null || image.getDeleted()) {
            return null;
        }
        return s3Uploader.getUrlFile(image.getFileKey());
    }
}
