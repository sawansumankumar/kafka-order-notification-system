package com.sawan.orderprocessing.notifications.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID eventId;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
