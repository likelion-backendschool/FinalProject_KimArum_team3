package com.ll.exam.FinalProject_KimArum.app.mybook.service;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.mybook.repository.MyBookRepository;
import com.ll.exam.FinalProject_KimArum.app.product.entity.Product;
import com.ll.exam.FinalProject_KimArum.app.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyBookService {
    private final ProductService productService;
    private final MyBookRepository myBookRepository;

    public void addMyBook(Member buyer, Long orderId) {
        List<Product> books = productService.findProductByOrderId(orderId);
    }
}
