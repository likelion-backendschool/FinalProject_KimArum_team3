package com.ll.exam.FinalProject_KimArum.app.withdraw.controller;

import com.ll.exam.FinalProject_KimArum.app.base.rq.Rq;
import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.member.service.MemberService;
import com.ll.exam.FinalProject_KimArum.app.security.dto.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/withdraw")
public class WithdrawController {
    private final MemberService memberService;
    private final Rq rq;

    @GetMapping
    @RequestMapping("/apply")
    public String showApplyWithdraw(Model model){
        Member member = rq.getMember();

        long restCash = memberService.getRestCash(member);
        System.out.println(restCash);

        model.addAttribute("restCash", restCash);

        return "withdraw/apply";
    }
}
