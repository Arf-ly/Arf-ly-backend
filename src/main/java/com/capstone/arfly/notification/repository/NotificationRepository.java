package com.capstone.arfly.notification.repository;

import com.capstone.arfly.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {}
