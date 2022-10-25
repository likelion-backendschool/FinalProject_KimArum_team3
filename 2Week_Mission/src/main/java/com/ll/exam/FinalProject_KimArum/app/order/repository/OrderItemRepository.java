package com.ll.exam.FinalProject_KimArum.app.order.repository;

import com.ll.exam.FinalProject_KimArum.app.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}