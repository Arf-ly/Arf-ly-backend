package com.capstone.arfly.notification.domain;

import com.capstone.arfly.common.domain.BaseCreatedEntity;
import com.capstone.arfly.common.domain.File;
import com.capstone.arfly.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

// 회원이 받은 알림 목록. 푸시 발송 여부와 관계없이 저장
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notification extends BaseCreatedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    // 객체아님
    // 알림 클릭 시 이동할 대상 ID (MENTION: 게시글 ID, JETSON_ALERT: 이상 징후 ID)
    @Column(nullable = false)
    private Long referenceId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    // 이상 징후 사진 (JetsonAlert와 같은 File 참조). 언급 알림은 null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private File image;

    // 푸시 알림과 같은 문구로 저장
    public static Notification mention(Member receiver, Long postId, String postTitle, String commenterNickName) {
        return Notification.builder()
                .member(receiver)
                .type(NotificationType.MENTION)
                .referenceId(postId)
                .title(postTitle)
                .content(commenterNickName + "님이 당신을 언급했습니다.")
                .build();
    }

    public static Notification jetsonAlert(
            Member receiver, Long alertId, String diseaseName, Double probability, File image) {
        return Notification.builder()
                .member(receiver)
                .type(NotificationType.JETSON_ALERT)
                .referenceId(alertId)
                .title("피부 이상 징후 감지")
                .content(String.format("%s 의심 (%.1f%%)", diseaseName, probability))
                .image(image)
                .build();
    }
}
