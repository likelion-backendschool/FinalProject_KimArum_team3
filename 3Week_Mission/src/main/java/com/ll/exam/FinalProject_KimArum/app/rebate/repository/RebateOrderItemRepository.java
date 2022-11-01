package com.ll.exam.FinalProject_KimArum.app.rebate.repository;

import com.ll.exam.FinalProject_KimArum.app.rebate.entity.RebateOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RebateOrderItemRepository extends JpaRepository<RebateOrderItem, Long> {
    Optional<RebateOrderItem> findByOrderItemId(long orderItemId);
}
