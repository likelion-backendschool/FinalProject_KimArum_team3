package com.ll.exam.FinalProject_KimArum.base.initData;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.member.service.MemberService;

public interface InitDataBefore {
    default void before(MemberService memberService) {
        Member member1 = memberService.join("user1", "1234", "user1@test.com", "hamster1");
        Member member2 = memberService.join("user2", "1234", "user2@test.com", "");
    }
}
