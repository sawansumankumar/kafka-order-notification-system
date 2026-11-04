package com.sawan.orderprocessing.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class OrderResponse {

    private final UUID id;
    private final String productName;
    private final Integer quantity;
    private final BigDecimal unitPrice;
    private final String status;
    private final Instant createdAt;
}