package com.ll.exam.FinalProject_KimArum.base.initData;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.member.service.MemberService;
import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;
import com.ll.exam.FinalProject_KimArum.app.post.service.PostService;
import com.ll.exam.FinalProject_KimArum.app.product.entity.Product;
import com.ll.exam.FinalProject_KimArum.app.product.service.ProductService;

public interface InitDataBefore {
    default void before(MemberService memberService, PostService postService, ProductService productService) {
        Member member1 = memberService.join("user1", "1234", "user1@test.com");
        Member member2 = memberService.join("user2", "1234", "user2@test.com");

        //member1 작가회원으로
        memberService.modify(member1, "user1@test.com", "햄스터");

        Post post1 = postService.writePost(member1.getId(), "1번 글", "1번 내용", "1번 내용", "#태그1 #태그2");
        Post post2 = postService.writePost(member1.getId(), "2번 글", "2번 내용", "2번 내용", "#태그2 #태그3");

        Product product1 = productService.create(member1, "1번 상품", 1200, 1, "#상품태그1 #상품태그2");
        Product product2 = productService.create(member1, "2번 상품", 2400, 2, "#상품태그2 #상품태그3");
    }
}
