package com.capstone.arfly.notification.repository;

import com.capstone.arfly.notification.domain.Notification;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    // 사진 URL 생성을 위해 image를 함께 조회 (언급 알림은 image가 없어 LEFT JOIN)
    @Query("SELECT n FROM Notification n "
            + "LEFT JOIN FETCH n.image "
            + "WHERE n.member.id = :memberId "
            + "AND (:cursor IS NULL OR n.id < :cursor) "
            + "ORDER BY n.id DESC")
    List<Notification> findNotificationsWithPaging(
            @Param("memberId") Long memberId, @Param("cursor") Long cursor, Pageable pageable);
}
