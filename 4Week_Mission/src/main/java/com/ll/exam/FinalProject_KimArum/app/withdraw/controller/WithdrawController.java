package com.ll.exam.FinalProject_KimArum.app.withdraw.controller;

import com.ll.exam.FinalProject_KimArum.app.base.dto.RsData;
import com.ll.exam.FinalProject_KimArum.app.base.rq.Rq;
import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.member.service.MemberService;
import com.ll.exam.FinalProject_KimArum.app.withdraw.form.WithdrawForm;
import com.ll.exam.FinalProject_KimArum.app.withdraw.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/withdraw")
public class WithdrawController {
    private final MemberService memberService;
    private final WithdrawService withdrawService;
    private final Rq rq;

    @GetMapping("/apply")
    @PreAuthorize("isAuthenticated()")
    public String showApplyWithdraw(Model model){
        Member member = rq.getMember();

        long restCash = memberService.getRestCash(member);
        System.out.println(restCash);

        model.addAttribute("restCash", restCash);

        return "withdraw/apply";
    }

    @PostMapping("/apply")
    @PreAuthorize("isAuthenticated()")
    public String applyWithdraw(@Valid WithdrawForm withdrawForm){
        RsData rsData = withdrawService.apply(rq.getId(), withdrawForm.getBankName(), withdrawForm.getBankAccountNo(), withdrawForm.getPrice());

        return Rq.redirectWithMsg("/withdraw/apply", rsData.getMsg());
    }

}
