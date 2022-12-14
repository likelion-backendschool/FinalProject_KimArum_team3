package com.ll.exam.FinalProject_KimArum.base.initData;

import com.ll.exam.FinalProject_KimArum.app.member.service.MemberService;
import com.ll.exam.FinalProject_KimArum.app.post.service.PostService;
import com.ll.exam.FinalProject_KimArum.app.product.service.ProductService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DevInitData implements InitDataBefore {
    @Bean
    CommandLineRunner initData(MemberService memberService, PostService postService, ProductService productService) {
        return args -> {
            before(memberService, postService, productService);
        };
    }
}
