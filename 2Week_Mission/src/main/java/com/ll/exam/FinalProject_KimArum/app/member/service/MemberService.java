package com.ll.exam.FinalProject_KimArum.app.member.service;

import com.ll.exam.FinalProject_KimArum.app.cash.entity.CashLog;
import com.ll.exam.FinalProject_KimArum.app.cash.service.CashService;
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
    private final CashService cashService;

    public Member join(String username, String password, String email) {
        if(memberRepository.findByUsername(username).isPresent()){
            throw new AlreadyJoinException();
        }

        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .authLevel(3)
                .build();

        memberRepository.save(member);

        return member;
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Optional<Member> findByNickname(String nickname) {
        return memberRepository.findByNickname(nickname);
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

    public void modifyPassword(Member member, String newPassword) {
        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
    }

    @Transactional
    public long addCash(Member member, long price, String eventType){
        CashLog cashLog = cashService.addCash(member, price, eventType);

        long newRestCash = member.getRestCash() + cashLog.getPrice();
        member.setRestCash(newRestCash);
        memberRepository.save(member);

        return newRestCash;
    }

    public long getRestCash(Member member){
        Member foundMember = findByUsername(member.getUsername()).get();

        return foundMember.getRestCash();
    }
}
