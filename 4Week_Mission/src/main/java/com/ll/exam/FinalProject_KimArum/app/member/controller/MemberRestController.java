package com.ll.exam.FinalProject_KimArum.app.member.controller;

import com.ll.exam.FinalProject_KimArum.app.base.dto.RsData;
import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.member.form.LoginForm;
import com.ll.exam.FinalProject_KimArum.app.member.service.MemberService;
import com.ll.exam.FinalProject_KimArum.app.security.dto.MemberContext;
import com.ll.exam.FinalProject_KimArum.util.Ut;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "MemberController", description = "로그인 기능과 로그인 된 회원의 정보를 제공 기능을 담당하는 컨트롤러")
public class MemberRestController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<RsData> login(@RequestBody LoginForm loginForm) {
        if (loginForm.isNotValid()) {
            return Ut.spring.responseEntityOf(RsData.of("F-1", "로그인 정보가 올바르지 않습니다."));
        }

        Member member = memberService.findByUsername(loginForm.getUsername()).orElse(null);

        if (member == null) {
            return Ut.spring.responseEntityOf(RsData.of("F-2", "일치하는 회원이 존재하지 않습니다."));
        }

        if (passwordEncoder.matches(loginForm.getPassword(), member.getPassword()) == false) {
            return Ut.spring.responseEntityOf(RsData.of("F-3", "비밀번호가 일치하지 않습니다."));
        }

        log.debug("Util.json.toStr(member.getAccessTokenClaims()) : " + Ut.json.toStr(member.getAccessTokenClaims()));

        String accessToken = memberService.genAccessToken(member);

        return Ut.spring.responseEntityOf(
                RsData.of(
                        "S-1",
                        "로그인 성공, Access Token을 발급합니다.",
                        Ut.mapOf(
                                "accessToken", accessToken
                        )
                ),
                Ut.spring.httpHeadersOf("Authentication", accessToken)
        );
    }

    @GetMapping("/me")
    public ResponseEntity<RsData> me(@AuthenticationPrincipal MemberContext memberContext){
        if(memberContext == null){
            return Ut.spring.responseEntityOf(RsData.failOf(null));
        }

        return Ut.spring.responseEntityOf(RsData.successOf(memberContext));
    }
}
