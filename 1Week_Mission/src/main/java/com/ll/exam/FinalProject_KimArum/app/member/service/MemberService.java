package com.ll.exam.FinalProject_KimArum.app.member.service;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.member.exception.AlreadyJoinException;
import com.ll.exam.FinalProject_KimArum.app.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member join(String username, String password, String email, String nickname) {
        if(memberRepository.findByUsername(username).isPresent()){
            throw new AlreadyJoinException();
        }

        int authLevel = 3;

        if(nickname!=null){
            authLevel = 7;
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .nickname(nickname)
                .authLevel(authLevel)
                .build();

        memberRepository.save(member);

        return member;
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public void modify(Member member, String email, String nickname) {
        member.setEmail(email);
        member.setNickname(nickname);

        if(nickname != null){
            member.setAuthLevel(7);
        }
        else {
            member.setAuthLevel(3);
        }

        memberRepository.save(member);
    }
}
