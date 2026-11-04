package com.sawan.orderprocessing.notifications.repository;

import com.sawan.orderprocessing.notifications.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    boolean existsByEventId(UUID eventId);
    List<Notification> findByOrderIdOrderByCreatedAtAsc(UUID orderId);
}
