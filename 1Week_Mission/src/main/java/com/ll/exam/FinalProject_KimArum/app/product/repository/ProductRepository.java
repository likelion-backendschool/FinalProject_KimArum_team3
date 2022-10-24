package com.ll.exam.FinalProject_KimArum.app.product.repository;

import com.ll.exam.FinalProject_KimArum.app.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {
    List<Product> findAllByOrderByIdDesc();

    Optional<Product> findProductById(Long id);
}
