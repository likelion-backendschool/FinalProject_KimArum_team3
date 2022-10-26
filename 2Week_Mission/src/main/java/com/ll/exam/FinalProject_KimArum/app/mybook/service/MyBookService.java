package com.ll.exam.FinalProject_KimArum.app.mybook.service;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.mybook.entity.MyBook;
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

        for(Product book : books){
            saveMyBook(buyer, book.getId());
        }
    }

    private MyBook saveMyBook(Member buyer, long bookId) {
        Product product = productService.getProductById(bookId);
        MyBook myBook = MyBook.builder()
                .buyer(buyer)
                .product(product)
                .build();

        myBookRepository.save(myBook);

        return myBook;
    }


    public void refundedMyBook(Member buyer, Long orderId) {
        List<Product> books = productService.findProductByOrderId(orderId);

        for(Product book : books){
            deleteMyBook(buyer, book.getId());
        }
    }

    private void deleteMyBook(Member buyer, long bookId){
        MyBook myBook = myBookRepository.findByBuyerIdAndProductId(buyer.getId(), bookId);

        myBookRepository.delete(myBook);
    }
}
