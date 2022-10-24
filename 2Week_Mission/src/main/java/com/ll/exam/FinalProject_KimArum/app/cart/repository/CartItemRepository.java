package com.ll.exam.FinalProject_KimArum.app.cart.repository;

import com.ll.exam.FinalProject_KimArum.app.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByBuyerIdAndProductId(long buyerId, long productId);

    boolean existsByBuyerIdAndProductId(long buyerId, long productId);
}
