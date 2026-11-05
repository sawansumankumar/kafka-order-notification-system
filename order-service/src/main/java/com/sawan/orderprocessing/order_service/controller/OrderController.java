package com.sawan.orderprocessing.order_service.controller;

import com.sawan.orderprocessing.order_service.dto.CreateOrderRequest;
import com.sawan.orderprocessing.order_service.dto.OrderResponse;
import com.sawan.orderprocessing.order_service.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        OrderResponse response;

        try {
            response = orderService.createOrder(request);
        } catch (RuntimeException exception) {
            log.error("Order creation failed", exception);
            throw exception;
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(orderService.getOrderById(id));
    }
}
