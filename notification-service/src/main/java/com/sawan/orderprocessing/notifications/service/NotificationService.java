package com.sawan.orderprocessing.notifications.service;

import com.sawan.orderprocessing.notifications.dto.NotificationResponse;
import com.sawan.orderprocessing.notifications.entity.Notification;
import com.sawan.orderprocessing.notifications.event.OrderCreatedEvent;
import com.sawan.orderprocessing.notifications.repository.NotificationRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void processOrderCreated(OrderCreatedEvent event)
    {
        if(notificationRepository.existsByEventId(event.getEventId()))
        {
            log.info("Skipping already processed event: {}", event.getEventId());
            return;
        }

        // Temporary failure simulation for testing retries and DLT.
        if ("FAIL_TEST".equals(event.getProductName())) {
            log.warn("Simulating failure for event: {}", event.getEventId());
            throw new IllegalStateException("Simulated notification failure");
        }

        Notification notification = new Notification();
        notification.setEventId(event.getEventId());
        notification.setOrderId(event.getOrderId());
        notification.setMessage(
                "Your Order for " + event.getQuantity()
                + " x " + event.getProductName()
                + " has been created"
        );

        notification.setCreatedAt(Instant.now());
        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsByOrderId(UUID orderId)
    {
        return notificationRepository.findByOrderIdOrderByCreatedAtAsc(orderId).stream().
                map(notification -> new NotificationResponse(notification.getId(),
                        notification.getOrderId(),notification.getMessage(),notification.getCreatedAt())).toList();
    }
}
