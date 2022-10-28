package com.ll.exam.FinalProject_KimArum.app.member.repository;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByUsernameAndEmail(String username, String email);

    Optional<Member> findByNickname(String nickname);
}
