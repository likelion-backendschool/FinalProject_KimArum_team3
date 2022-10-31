package com.ll.exam.FinalProject_KimArum.app.security.service;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.member.entity.emum.AuthLevel;
import com.ll.exam.FinalProject_KimArum.app.member.repository.MemberRepository;
import com.ll.exam.FinalProject_KimArum.app.security.dto.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username).get();

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (member.getAuthLevel() == AuthLevel.NORMAL){
            authorities.add(new SimpleGrantedAuthority("MEMBER"));
        }
        else if(member.getAuthLevel() == AuthLevel.ADMIN){
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        }

        return new MemberContext(member, member.genAuthorities());
    }
}
