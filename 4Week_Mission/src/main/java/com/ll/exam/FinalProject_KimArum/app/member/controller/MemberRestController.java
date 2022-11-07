package com.ll.exam.FinalProject_KimArum.app.member.controller;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.member.form.LoginForm;
import com.ll.exam.FinalProject_KimArum.app.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberRestController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginForm loginForm) {
        if (loginForm.isNotValid()) {
            return new ResponseEntity<>(null, null, HttpStatus.BAD_REQUEST);
        }

        Member member = memberService.findByUsername(loginForm.getUsername()).orElse(null);

        if(member == null){
            return new ResponseEntity<>(null, null, HttpStatus.BAD_REQUEST);
        }

        if (passwordEncoder.matches(loginForm.getPassword(), member.getPassword()) == false) {
            return new ResponseEntity<>(null, null, HttpStatus.BAD_REQUEST);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authentication", "JWT키");

        String body = "username : %s, password : %s".formatted(loginForm.getUsername(), loginForm.getPassword());

        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }
}
