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

    public Optional<ProductKeyword> findByContent(String content) {
        return productKeywordRepository.findByContent(content);
    }

    public ProductKeyword findByContentOrSave(String content) {
        return save(content);
    }

    public Optional<ProductKeyword> findById(long id) {
        return productKeywordRepository.findById(id);
    }
}
