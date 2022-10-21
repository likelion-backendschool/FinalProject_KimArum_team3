package com.ll.exam.FinalProject_KimArum.app.member.controller;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.member.form.JoinForm;
import com.ll.exam.FinalProject_KimArum.app.member.form.ModifyForm;
import com.ll.exam.FinalProject_KimArum.app.member.form.ModifyPasswordForm;
import com.ll.exam.FinalProject_KimArum.app.member.service.MemberService;
import com.ll.exam.FinalProject_KimArum.app.secutiry.dto.MemberContext;
import com.ll.exam.FinalProject_KimArum.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/join")
    public String showJoin(){
        return "member/join";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/join")
    public String join(@Valid JoinForm joinForm){
        if(memberService.findByUsername(joinForm.getUsername()).isPresent()) {
            return "redirect:/member/join?errorMsg=" + Util.url.encode("이미 사용 중인 아이디입니다.");
        }

        if(memberService.findByEmail(joinForm.getEmail()).isPresent()) {
            return "redirect:/member/join?errorMsg=" + Util.url.encode("이미 사용 중인 이메일입니다.");
        }

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
        else if(memberService.findByNickname(modifyForm.getNickname()).isPresent()) {
            return "redirect:/member/modify?errorMsg=" + Util.url.encode("이미 사용 중인 닉네임입니다.");
        }

        memberService.modify(member, modifyForm.getEmail(), nickname);

        context.setUpdateDate(member.getUpdateDate());
        context.setEmail(member.getEmail());
        context.setNickname(member.getNickname());
        Authentication authentication = new UsernamePasswordAuthenticationToken(context, member.getPassword(), context.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/member/profile";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modifyPassword")
    public String showModifyPassword(){
        return "member/modifyPassword";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modifyPassword")
    public String modifyPassword(@AuthenticationPrincipal MemberContext context, @Valid ModifyPasswordForm modifyPasswordForm){
        Member member = memberService.findByUsername(context.getUsername()).get();

        if(passwordEncoder.matches(modifyPasswordForm.getOldPassword(), member.getPassword())==false){
            return "redirect:/member/modifyPassword?errorMsg=" + Util.url.encode("기존 비밀번호를 다시 입력해주세요.");
        }

        if(!modifyPasswordForm.getNewPassword().equals(modifyPasswordForm.getNewPasswordConfirm())){
            return "redirect:/member/modifyPassword?errorMsg=" + Util.url.encode("비밀번호 확인이 일치하지 않습니다.");
        }

        memberService.modifyPassword(member, modifyPasswordForm.getNewPassword());

        Authentication authentication = new UsernamePasswordAuthenticationToken(context, member.getPassword(), context.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/member/profile?msg=" + Util.url.encode("비밀번호가 변경되었습니다.");
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
