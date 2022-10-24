package com.ll.exam.FinalProject_KimArum;

import com.ll.exam.FinalProject_KimArum.app.cart.entity.CartItem;
import com.ll.exam.FinalProject_KimArum.app.cart.service.CartService;
import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.member.repository.MemberRepository;
import com.ll.exam.FinalProject_KimArum.app.product.entity.Product;
import com.ll.exam.FinalProject_KimArum.app.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class CartTests {
    @Autowired
    private ProductService productService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CartService cartService;

    @Test
    @DisplayName("장바구니에 담기")
    void t1() {
        Member buyer = memberRepository.findByUsername("user1").get();

        Product product1 = productService.getProductById(1L);
        Product product2 = productService.getProductById(2L);

        CartItem cartItem1 = cartService.addItem(buyer, product1);
        CartItem cartItem2 = cartService.addItem(buyer, product2);

        assertThat(cartItem1).isNotNull();
        assertThat(cartItem2).isNotNull();
    }

    @Test
    @DisplayName("장바구니에서 제거")
    void t2() {
        Member buyer1 = memberRepository.findByUsername("user1").get();

        Product product1 = productService.getProductById(1L);
        Product product2 = productService.getProductById(2L);

        cartService.removeItem(buyer1, product1);
        cartService.removeItem(buyer1, product2);

        assertThat(cartService.hasItem(buyer1, product1)).isFalse();
        assertThat(cartService.hasItem(buyer1, product2)).isFalse();
    }
}
