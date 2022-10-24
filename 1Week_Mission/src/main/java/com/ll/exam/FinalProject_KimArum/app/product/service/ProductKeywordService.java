package com.ll.exam.FinalProject_KimArum.app.product.service;

import com.ll.exam.FinalProject_KimArum.app.product.entity.ProductKeyword;
import com.ll.exam.FinalProject_KimArum.app.product.repository.ProductKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductKeywordService {
    private final ProductKeywordRepository productKeywordRepository;

    public ProductKeyword save(String content) {
        Optional<ProductKeyword> optKeyword = productKeywordRepository.findByContent(content);

        if (optKeyword.isPresent()) {
            return optKeyword.get();
        }

        ProductKeyword productKeyword = ProductKeyword
                .builder()
                .content(content)
                .build();

        productKeywordRepository.save(productKeyword);

        return productKeyword;
    }
}
