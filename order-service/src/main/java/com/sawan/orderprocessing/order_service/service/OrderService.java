package com.sawan.orderprocessing.order_service.service;

import com.sawan.orderprocessing.order_service.dto.CreateOrderRequest;
import com.sawan.orderprocessing.order_service.dto.OrderResponse;
import com.sawan.orderprocessing.order_service.entity.Order;
import com.sawan.orderprocessing.order_service.event.OrderCreatedEvent;
import com.sawan.orderprocessing.order_service.producer.OrderEventProducer;
import com.sawan.orderprocessing.order_service.repository.OrderRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventProducer orderEventProducer;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {
        Order order = new Order();
        order.setProductName(request.getProductName().trim());
        order.setQuantity(request.getQuantity());
        order.setUnitPrice(request.getUnitPrice());
        order.setStatus("CREATED");
        order.setCreatedAt(Instant.now());

        Order savedOrder = orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(
                UUID.randomUUID(),
                savedOrder.getId(),
                savedOrder.getProductName(),
                savedOrder.getQuantity(),
                savedOrder.getUnitPrice(),
                savedOrder.getCreatedAt()
        );

        orderEventProducer.publish(event).join();

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getProductName(),
                savedOrder.getQuantity(),
                savedOrder.getUnitPrice(),
                savedOrder.getStatus(),
                savedOrder.getCreatedAt()
        );
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(UUID id)
    {
        Order order = orderRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        return new OrderResponse(order.getId(), order.getProductName(), order.getQuantity(),
                order.getUnitPrice(), order.getStatus(), order.getCreatedAt());
    }
}
