package com.ll.exam.FinalProject_KimArum.base.initData;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.member.service.MemberService;
import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;
import com.ll.exam.FinalProject_KimArum.app.post.service.PostService;

public interface InitDataBefore {
    default void before(MemberService memberService, PostService postService) {
        Member member1 = memberService.join("user1", "1234", "user1@test.com", "hamster1");
        Member member2 = memberService.join("user2", "1234", "user2@test.com", null);

        Post post1 = postService.writePost(member1.getId(), "1번 글", "1번 내용", "1번 내용");
        Post post2 = postService.writePost(member1.getId(), "2번 글", "2번 내용", "2번 내용");
    }
}
