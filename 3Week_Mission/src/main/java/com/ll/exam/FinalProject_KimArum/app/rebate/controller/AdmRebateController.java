package com.ll.exam.FinalProject_KimArum.app.rebate.controller;

import com.ll.exam.FinalProject_KimArum.app.base.dto.RsData;
import com.ll.exam.FinalProject_KimArum.app.base.rq.Rq;
import com.ll.exam.FinalProject_KimArum.app.order.entity.OrderItem;
import com.ll.exam.FinalProject_KimArum.app.rebate.entity.RebateOrderItem;
import com.ll.exam.FinalProject_KimArum.app.rebate.service.RebateService;
import com.ll.exam.FinalProject_KimArum.util.Ut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/adm/rebate")
@RequiredArgsConstructor
@Slf4j
public class AdmRebateController {
    private final RebateService rebateService;

    @GetMapping("/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showMakeData() {
        return "adm/rebate/makeData";
    }

    @PostMapping("/makeData")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String makeData(String yearMonth) {
        RsData makeDateRsData = rebateService.makeDate(yearMonth);

        return Rq.redirectWithMsg("/adm/rebate/rebateOrderItemList?yearMonth=%s".formatted(yearMonth), makeDateRsData);
    }

    @GetMapping("/rebateOrderItemList")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String showRebateOrderItemList(String yearMonth, Model model) {
        if (yearMonth == null) {
            yearMonth = "2022-11";
        }

        List<RebateOrderItem> items = rebateService.findRebateOrderItemsByPayDateIn(yearMonth);

        model.addAttribute("items", items);

        return "adm/rebate/rebateOrderItemList";
    }

    @PostMapping("/rebateOne/{orderItemId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String rebateOne(@PathVariable long orderItemId, HttpServletRequest req) {
        RsData rebateRsData = rebateService.rebate(orderItemId);

        String referer = req.getHeader("Referer");
        log.debug("referer : " + referer);
        String yearMonth = Ut.url.getQueryParamValue(referer, "yearMonth", "");

        return Rq.redirectWithMsg("/adm/rebate/rebateOrderItemList?yearMonth=%s".formatted(yearMonth), rebateRsData);
    }

    @PostMapping("/rebate")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String rebate(String ids, HttpServletRequest req) {

        String[] idsArr = ids.split(",");

        Arrays.stream(idsArr)
                .mapToLong(Long::parseLong)
                .forEach(id -> {
                    rebateService.rebate(id);
                });

        String referer = req.getHeader("Referer");
        String yearMonth = Ut.url.getQueryParamValue(referer, "yearMonth", "");

        return Rq.redirectWithMsg("/adm/rebate/rebateOrderItemList?yearMonth=%s".formatted(yearMonth), "%d건의 정산품목을 정산처리하였습니다.".formatted(idsArr.length));
    }
}
