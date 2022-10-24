package com.ll.exam.FinalProject_KimArum.app.product.repository;

import com.ll.exam.FinalProject_KimArum.app.product.entity.ProductKeyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductKeywordRepository extends JpaRepository<ProductKeyword, Long> {
    Optional<ProductKeyword> findByContent(String keywordContent);
}
