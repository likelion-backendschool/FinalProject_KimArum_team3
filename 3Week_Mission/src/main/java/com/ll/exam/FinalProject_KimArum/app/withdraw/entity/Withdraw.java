package com.ll.exam.FinalProject_KimArum.app.withdraw.entity;

import com.ll.exam.FinalProject_KimArum.app.base.entity.BaseEntity;
import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Withdraw extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Member member;

    private String bankName;

    private String bankAccountNo;

    private long price;
}