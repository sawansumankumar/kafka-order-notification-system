package com.sawan.orderprocessing.order_service.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreatedEvent {

    private UUID eventId;
    private UUID orderId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private Instant createdAt;
}