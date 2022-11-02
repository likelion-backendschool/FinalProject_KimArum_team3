package com.ll.exam.FinalProject_KimArum.app.withdraw.service;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.member.service.MemberService;
import com.ll.exam.FinalProject_KimArum.app.withdraw.entity.Withdraw;
import com.ll.exam.FinalProject_KimArum.app.withdraw.repository.WithdrawRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WithdrawService {
    private final WithdrawRepository withdrawRepository;
    private final MemberService memberService;

    @Transactional
    public void apply(Member member, String bankName, String bankAccountNo, long price) {
        String bank = bankName.replaceAll("은행", "").trim();
        bank = bank + "은행";

        Withdraw withdraw = Withdraw.builder()
                .member(member)
                .bankName(bank)
                .bankAccountNo(bankAccountNo)
                .price(price)
                .build();

        withdrawRepository.save(withdraw);

        //memberService.addCash(member, price * -1, "출금신청");
    }
}