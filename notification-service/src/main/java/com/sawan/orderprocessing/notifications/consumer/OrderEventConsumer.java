package com.sawan.orderprocessing.notifications.consumer;

import com.sawan.orderprocessing.notifications.event.OrderCreatedEvent;
import com.sawan.orderprocessing.notifications.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventConsumer {

    private final NotificationService notificationService;

    @KafkaListener(topics = "order-created")
    public void consume(OrderCreatedEvent event) {
        log.info("Received order-created event: {}", event.getEventId());
        notificationService.processOrderCreated(event);

        log.info("Processed order-created event: {}", event.getEventId());
    }

}
