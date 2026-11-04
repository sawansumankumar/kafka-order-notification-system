package com.sawan.orderprocessing.order_service.repository;

import com.sawan.orderprocessing.order_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
}
