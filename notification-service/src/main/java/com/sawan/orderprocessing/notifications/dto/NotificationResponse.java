package com.sawan.orderprocessing.notifications.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class NotificationResponse {

    private final UUID id;
    private final UUID orderId;
    private final String message;
    private final Instant createdAt;

}
