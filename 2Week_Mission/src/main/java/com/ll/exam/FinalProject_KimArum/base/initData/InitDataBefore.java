package com.ll.exam.FinalProject_KimArum.base.initData;

import com.ll.exam.FinalProject_KimArum.app.cart.entity.CartItem;
import com.ll.exam.FinalProject_KimArum.app.cart.service.CartService;
import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.member.service.MemberService;
import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;
import com.ll.exam.FinalProject_KimArum.app.post.service.PostService;
import com.ll.exam.FinalProject_KimArum.app.product.entity.Product;
import com.ll.exam.FinalProject_KimArum.app.product.service.ProductService;

public interface InitDataBefore {
    default void before(MemberService memberService, PostService postService, ProductService productService, CartService cartService) {
        Member member1 = memberService.join("user1", "1234", "user1@test.com");
        Member member2 = memberService.join("user2", "1234", "user2@test.com");

        //member1 작가회원으로
        memberService.modify(member1, "user1@test.com", "햄스터");

        Post post1 = postService.writePost(member1.getId(), "1번 글", "1번 내용", "1번 내용", "#태그1 #태그2");
        Post post2 = postService.writePost(member1.getId(), "2번 글", "2번 내용", "2번 내용", "#태그2 #태그3");
        Post post3 = postService.writePost(member1.getId(), "3번 글", "3번 내용", "3번 내용", "#태그2 #태그3 #태그4");
        Post post4 = postService.writePost(member1.getId(), "4번 글", "4번 내용", "4번 내용", "#태그4 #태그5");

        Product product1 = productService.create(member1, "1번 상품", 1200, 1, "#상품태그1 #상품태그2");
        Product product2 = productService.create(member1, "2번 상품", 2400, 2, "#상품태그2 #상품태그3");
        Product product3 = productService.create(member1, "3번 상품", 3600, 3, "#상품태그1 #상품태그2");
        Product product4 = productService.create(member1, "4번 상품", 4800, 4, "#상품태그2 #상품태그3");

        CartItem cartItem1 = cartService.addItem(member2, product1);
        CartItem cartItem2 = cartService.addItem(member2, product2);
    }
}
