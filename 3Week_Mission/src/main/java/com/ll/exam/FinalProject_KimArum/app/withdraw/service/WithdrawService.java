package com.ll.exam.FinalProject_KimArum.app.withdraw.service;

import com.ll.exam.FinalProject_KimArum.app.base.dto.RsData;
import com.ll.exam.FinalProject_KimArum.app.cash.entity.CashLog;
import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.member.service.MemberService;
import com.ll.exam.FinalProject_KimArum.app.withdraw.entity.Withdraw;
import com.ll.exam.FinalProject_KimArum.app.withdraw.repository.WithdrawRepository;
import com.ll.exam.FinalProject_KimArum.util.Ut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WithdrawService {
    private final WithdrawRepository withdrawRepository;
    private final MemberService memberService;

    public RsData<Withdraw> apply(long memberId, String bankName, String bankAccountNo, long price) {
        Member member = memberService.findById(memberId).orElse(null);

        if(member==null){
            return RsData.of("F-1", "존재하지 않는 회원입니다.");
        }

        if(memberService.getRestCash(member)<price){
            return RsData.of("F-2", "출금 금액 초과입니다.");
        }

        CashLog cashLog = memberService.addCash(
                member,
                price * -1,
                "출금신청__예치금"
        ).getData().getCashLog();

        Withdraw withdraw = Withdraw.builder()
                .member(member)
                .bankName(bankName)
                .bankAccountNo(bankAccountNo)
                .price(price)
                .withdrawCashLog(cashLog)
                .build();

        withdrawRepository.save(withdraw);

        return RsData.of("S-1", "출금 신청이 완료되었습니다.", withdraw);
    }

    public List<Withdraw> findAllByOrderByIdDesc() {
        return withdrawRepository.findAllByOrderByIdDesc();
    }

    public RsData withdraw(Long withdrawApplyId) {
        Withdraw withdraw = withdrawRepository.findById(withdrawApplyId).orElse(null);

        if (withdraw == null) {
            return RsData.of("F-1", "출금신청 데이터를 찾을 수 없습니다.");
        }

        if (withdraw.withdrawAvailable() == false) {
            return RsData.of("F-2", "이미 처리되었습니다.");
        }

        withdraw.setWithdrawDone(withdraw.getWithdrawCashLog().getId());

        return RsData.of(
                "S-1",
                "출금신청(%d번) 처리완료. %s원이 출금되었습니다.".formatted(withdraw.getId(), Ut.nf(withdraw.getPrice())),
                Ut.mapOf(
                        "cashLogId", withdraw.getWithdrawCashLog().getId()
                )
        );
    }
}