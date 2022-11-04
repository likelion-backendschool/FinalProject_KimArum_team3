package com.ll.exam.FinalProject_KimArum.app.withdraw.controller;

import com.ll.exam.FinalProject_KimArum.app.base.dto.RsData;
import com.ll.exam.FinalProject_KimArum.app.base.rq.Rq;
import com.ll.exam.FinalProject_KimArum.app.withdraw.entity.Withdraw;
import com.ll.exam.FinalProject_KimArum.app.withdraw.service.WithdrawService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/adm/withdraw")
@Slf4j
public class AdmWithdrawController {
    private final WithdrawService withdrawService;

    @GetMapping("/applyList")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showApplyList(Model model){
        List<Withdraw> withdrawList = withdrawService.findAllByOrderByIdDesc();

        model.addAttribute("withdrawList", withdrawList);

        return "adm/withdraw/withdrawList";
    }

    @PostMapping("/withdrawOne/{withdrawId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String rebateOne(@PathVariable long withdrawId) {
        RsData withdrawRsData = withdrawService.withdraw(withdrawId);

        return Rq.redirectWithMsg("/adm/withdraw/applyList", withdrawRsData.getMsg());
    }
}
