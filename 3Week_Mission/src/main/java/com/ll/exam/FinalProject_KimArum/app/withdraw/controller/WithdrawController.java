package com.ll.exam.FinalProject_KimArum.app.withdraw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/withdraw")
public class WithdrawController {

    @GetMapping
    @RequestMapping("/apply")
    public String showApplyWithdraw(){
        return "withdraw/apply";
    }
}
