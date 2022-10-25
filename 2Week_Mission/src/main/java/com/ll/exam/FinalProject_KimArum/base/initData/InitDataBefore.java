package com.ll.exam.FinalProject_KimArum.base.initData;

import com.ll.exam.FinalProject_KimArum.app.cart.entity.CartItem;
import com.ll.exam.FinalProject_KimArum.app.cart.service.CartService;
import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.member.service.MemberService;
import com.ll.exam.FinalProject_KimArum.app.order.entity.Order;
import com.ll.exam.FinalProject_KimArum.app.order.service.OrderService;
import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;
import com.ll.exam.FinalProject_KimArum.app.post.service.PostService;
import com.ll.exam.FinalProject_KimArum.app.product.entity.Product;
import com.ll.exam.FinalProject_KimArum.app.product.service.ProductService;

import java.util.Arrays;
import java.util.List;

public interface InitDataBefore {
    default void before(MemberService memberService, PostService postService, ProductService productService, CartService cartService, OrderService orderService) {
        class Helper {
            public Order order(Member member, List<Product> products) {
                for (int i = 0; i < products.size(); i++) {
                    Product product = products.get(i);

                    cartService.addItem(member, product);
                }

                return orderService.createFromCart(member);
            }
        }

        Helper helper = new Helper();
        Member member1 = memberService.join("user1", "1234", "user1@test.com");
        Member member2 = memberService.join("user2", "1234", "user2@test.com");
        Member member3 = memberService.join("user3", "1234", "user3@test.com");

        //member1,3 작가회원으로
        memberService.modify(member1, "user1@test.com", "햄스터");
        memberService.modify(member3, "user3@test.com", "나옹이");

        Post post1 = postService.writePost(member1.getId(), "1번 글", "1번 내용", "1번 내용", "#태그1 #태그2");
        Post post2 = postService.writePost(member1.getId(), "2번 글", "2번 내용", "2번 내용", "#태그2 #태그3");
        Post post3 = postService.writePost(member1.getId(), "3번 글", "3번 내용", "3번 내용", "#태그2 #태그3 #태그4");
        Post post4 = postService.writePost(member1.getId(), "4번 글", "4번 내용", "4번 내용", "#태그4 #태그5");
        Post post5 = postService.writePost(member3.getId(), "5번 글", "5번 내용", "5번 내용", "#태그4 #태그5");
        Post post6 = postService.writePost(member3.getId(), "6번 글", "6번 내용", "6번 내용", "#태그4 #태그5");

        Product product1 = productService.create(member1, "1번 상품", 1200, 1, "#상품태그1 #상품태그2");
        Product product2 = productService.create(member1, "2번 상품", 2400, 2, "#상품태그2 #상품태그3");
        Product product3 = productService.create(member1, "3번 상품", 3600, 3, "#상품태그1 #상품태그2");
        Product product4 = productService.create(member1, "4번 상품", 4800, 4, "#상품태그2 #상품태그3");
        Product product5 = productService.create(member1, "5번 상품", 1000, 4, "#상품태그4 #상품태그5");
        Product product6 = productService.create(member3, "6번 상품", 1000, 4, "#상품태그5");

        CartItem cartItem1 = cartService.addItem(member2, product1);
        CartItem cartItem2 = cartService.addItem(member2, product2);

        memberService.addCash(member2, 100_000, "충전__무통장입금");
        memberService.addCash(member2, 20_000, "충전__무통장입금");
        memberService.addCash(member2, -5_000, "출금__일반");

        memberService.addCash(member1, 2_000_000, "충전__무통장입금");

        // 1번 주문 : 결제완료
        Order order1 = helper.order(member2, Arrays.asList(
                        product1
                )
        );

        int order1PayPrice = order1.calculatePayPrice();
        orderService.payByRestCashOnly(order1);

        // 2번 주문 : 결제 후 환불
        Order order2 = helper.order(member2, Arrays.asList(
                        product3,
                        product4
                )
        );

        orderService.payByRestCashOnly(order2);

        orderService.refund(order2);

        // 3번 주문 : 결제 전
        Order order3 = helper.order(member2, Arrays.asList(
                        product2,
                        product5
                )
        );
    }
}
