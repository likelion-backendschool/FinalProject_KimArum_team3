package com.ll.exam.FinalProject_KimArum.app.member.controller;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.member.form.JoinForm;
import com.ll.exam.FinalProject_KimArum.app.member.form.ModifyForm;
import com.ll.exam.FinalProject_KimArum.app.member.service.MemberService;
import com.ll.exam.FinalProject_KimArum.app.secutiry.dto.MemberContext;
import com.ll.exam.FinalProject_KimArum.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Optional;

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
                joinForm.getEmail());

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify")
    public String showModify(){
        return "member/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify")
    public String modify(@AuthenticationPrincipal MemberContext context, @Valid ModifyForm modifyForm){
        Member member = memberService.findByUsername(modifyForm.getUsername()).get();

        String nickname = modifyForm.getNickname();
        if(nickname == ""){
            nickname = null;
        }

        memberService.modify(member, modifyForm.getEmail(), nickname);

        context.setUpdateDate(member.getUpdateDate());
        context.setEmail(member.getEmail());
        context.setNickname(member.getNickname());
        Authentication authentication = new UsernamePasswordAuthenticationToken(context, member.getPassword(), context.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/member/profile";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/findUsername")
    public String showFindUserName(){
        return "member/findUserName";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/findUsername")
    public String findUserName(@RequestParam String email, Model model){
        Optional<Member> member = memberService.findByEmail(email);

        if(member.isEmpty()){
            return "redirect:/member/findUsername?errorMsg=" + Util.url.encode("존재하지 않는 회원정보입니다.");
        }

        String username = member.get().getUsername();

        model.addAttribute("email", email);
        model.addAttribute("username", username);

        return "/member/findUsername";
    }
}
