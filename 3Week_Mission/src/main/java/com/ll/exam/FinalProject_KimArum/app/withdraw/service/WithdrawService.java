package com.ll.exam.FinalProject_KimArum.app.withdraw.service;

import com.ll.exam.FinalProject_KimArum.app.base.dto.RsData;
import com.ll.exam.FinalProject_KimArum.app.cash.entity.CashLog;
import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.member.service.MemberService;
import com.ll.exam.FinalProject_KimArum.app.withdraw.entity.Withdraw;
import com.ll.exam.FinalProject_KimArum.app.withdraw.repository.WithdrawRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WithdrawService {
    private final WithdrawRepository withdrawRepository;
    private final MemberService memberService;

    @Transactional
    public RsData apply(Long memberId, String bankName, String bankAccountNo, long price) {
        Member member = memberService.findById(memberId).orElse(null);

        if(member==null){
            return RsData.of("F-1", "존재하지 않는 회원입니다.");
        }

        if(memberService.getRestCash(member)<price){
            return RsData.of("F-2", "출금 금액 초과입니다.");
        }

        String bank = bankName.replaceAll("은행", "").trim();
        bank = bank + "은행";

        CashLog cashLog = memberService.addCash(
                member,
                price * -1,
                "출금신청__예치금")
                .getData().getCashLog();

        Withdraw withdraw = Withdraw.builder()
                .member(member)
                .bankName(bank)
                .bankAccountNo(bankAccountNo)
                .price(price)
                .withdrawCashLog(cashLog)
                .build();

        withdrawRepository.save(withdraw);

        return RsData.of("S-1", "출금 신청이 완료되었습니다.");
    }

    public List<Withdraw> findAllByOrderByIdDesc() {
        return withdrawRepository.findAllByOrderByIdDesc();
    }
}