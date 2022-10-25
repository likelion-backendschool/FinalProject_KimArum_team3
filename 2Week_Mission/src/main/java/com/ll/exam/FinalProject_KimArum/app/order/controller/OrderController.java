package com.ll.exam.FinalProject_KimArum.app.order.controller;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.order.entity.Order;
import com.ll.exam.FinalProject_KimArum.app.order.service.OrderService;
import com.ll.exam.FinalProject_KimArum.app.secutiry.dto.MemberContext;
import com.ll.exam.FinalProject_KimArum.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("{id}")
    @PreAuthorize("isAuthenticated()")
    public String showDetail(@AuthenticationPrincipal MemberContext memberContext, @PathVariable long id, Model model){
        Order order = orderService.findById(id).get();

        Member actor = memberContext.getMember();

        if(orderService.actorCanSee(actor, order) == false){
            return "redirect:/product/list?errorMsg=" + Util.url.encode("사용자의 주문이 아닙니다.");
        }

        model.addAttribute("order", order);

        return "order/detail";
    }
}
