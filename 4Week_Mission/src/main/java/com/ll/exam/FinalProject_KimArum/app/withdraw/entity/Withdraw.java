package com.ll.exam.FinalProject_KimArum.app.withdraw.entity;

import com.ll.exam.FinalProject_KimArum.app.base.entity.BaseEntity;
import com.ll.exam.FinalProject_KimArum.app.cash.entity.CashLog;
import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import java.time.LocalDateTime;

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

    private long price; //신청금액

    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    private CashLog withdrawCashLog; // 출금에 관련된 내역
    private LocalDateTime withdrawDate;

    public Withdraw(long id) {
        super(id);
    }

    public boolean withdrawAvailable() {
        if (withdrawDate != null) {
            return false;
        }

        return true;
    }

    public void setWithdrawDone(Long cashLogId) {
        withdrawDate = LocalDateTime.now();
        this.withdrawCashLog = new CashLog(cashLogId);
    }
}