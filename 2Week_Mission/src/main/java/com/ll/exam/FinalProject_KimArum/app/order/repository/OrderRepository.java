package com.ll.exam.FinalProject_KimArum.app.order.repository;

import com.ll.exam.FinalProject_KimArum.app.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
