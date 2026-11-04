package com.sawan.orderprocessing.notifications.controller;

import com.sawan.orderprocessing.notifications.dto.NotificationResponse;
import com.sawan.orderprocessing.notifications.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public List<NotificationResponse> getNotifications(@RequestParam UUID orderId) {
        return notificationService.getNotificationsByOrderId(orderId);
    }
}
