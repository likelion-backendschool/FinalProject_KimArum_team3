package com.ll.exam.FinalProject_KimArum.app.member.dto;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDto {

    Long id;
    LocalDateTime createDate;
    LocalDateTime modifyDate;
    String username;
    String email;
    boolean emailVerified;
    String nickname;

    public static MemberDto memberToMemberDto(Member member) {

        MemberDto memberDto = MemberDto.builder()
                .id(member.getId())
                .createDate(member.getCreateDate())
                .modifyDate(member.getModifyDate())
                .username(member.getUsername())
                .email(member.getEmail())
                .emailVerified(true)
                .nickname(member.getNickname())
                .build();

        return memberDto;
    }
}
