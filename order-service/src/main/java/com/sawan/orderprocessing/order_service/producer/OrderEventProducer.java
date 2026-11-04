package com.sawan.orderprocessing.order_service.producer;

import com.sawan.orderprocessing.order_service.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    public CompletableFuture<SendResult<String, OrderCreatedEvent>> publish(OrderCreatedEvent event)
    {
        return kafkaTemplate.send("order-created", event.getOrderId().toString(), event);
    }
}
