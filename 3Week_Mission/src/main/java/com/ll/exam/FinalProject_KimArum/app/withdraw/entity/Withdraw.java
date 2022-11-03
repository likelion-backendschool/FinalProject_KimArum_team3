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
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private CashLog withdrawCashLog; // 출금에 관련된 환급지급내역
    private LocalDateTime withdrawDate;  //출금처리일


    public void setWithdrawDone(long cashLogId) {
        withdrawDate = LocalDateTime.now();
        this.withdrawCashLog = new CashLog(cashLogId);
    }

    public boolean withdrawAvailable(){
        if (withdrawDate != null || withdrawCashLog != null) {
            return false;
        }

        return true;
    }
}