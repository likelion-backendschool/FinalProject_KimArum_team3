package com.ll.exam.FinalProject_KimArum.app.product.repository;

import com.ll.exam.FinalProject_KimArum.app.product.entity.Product;

import java.util.List;

public interface ProductRepositoryCustom {
    List<Product> searchQsl(String kwType, String kw);

    List<Product> findProductByOrderId(Long orderId);
}
