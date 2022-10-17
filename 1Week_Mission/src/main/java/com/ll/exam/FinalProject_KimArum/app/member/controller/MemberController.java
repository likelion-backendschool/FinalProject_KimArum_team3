package com.ll.exam.FinalProject_KimArum.app.member.controller;

import com.ll.exam.FinalProject_KimArum.app.member.form.JoinForm;
import com.ll.exam.FinalProject_KimArum.app.member.service.MemberService;
import com.ll.exam.FinalProject_KimArum.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin(){
        return "member/join";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm){
        memberService.join(joinForm.getUsername(),
                joinForm.getPassword(),
                joinForm.getEmail(),
                joinForm.getNickname());

        return "redirect:/member/login?msg=" + Util.url.encode("회원가입이 완료되었습니다.");
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showLogin(){
        return "member/login";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/profile")
    public String showProfile(){
        return "member/profile";
    }
}
