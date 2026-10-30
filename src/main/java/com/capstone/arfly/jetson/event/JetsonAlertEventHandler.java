package com.capstone.arfly.jetson.event;

import com.capstone.arfly.common.service.FirebaseService;
import com.capstone.arfly.notification.domain.FcmToken;
import com.capstone.arfly.notification.repository.FcmTokenRepository;
import com.capstone.arfly.notification.service.NotificationService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class JetsonAlertEventHandler {
    private final FcmTokenRepository fcmTokenRepository;
    private final FirebaseService firebaseService;
    private final NotificationService notificationService;

    // 이상 징후 저장이 커밋된 뒤 기기 주인에게 푸시 알림 발송
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleJetsonAlertCreatedEvent(JetsonAlertCreatedEvent event) {
        List<FcmToken> fcmTokens = fcmTokenRepository.findByMemberIdAndNotificationEnabled(Set.of(event.getOwnerId()));
        // 알림을 껐거나 등록된 FCM 토큰이 없으면 발송하지 않음
        if (fcmTokens.isEmpty()) {
            return;
        }

        Set<Long> failedFcmTokenIds =
                firebaseService.sendJetsonAlertNotifications(event.getDiseaseName(), event.getProbability(), fcmTokens);
        List<Long> successFcmTokenIds = fcmTokens.stream()
                .map(FcmToken::getId)
                .filter(tokenId -> !failedFcmTokenIds.contains(tokenId))
                .distinct()
                .toList();
        notificationService.updateCommentResults(successFcmTokenIds, failedFcmTokenIds);
        log.info("젯슨 이상 징후 푸시 알림 발송 완료 - memberId: {}", event.getOwnerId());
    }
}
