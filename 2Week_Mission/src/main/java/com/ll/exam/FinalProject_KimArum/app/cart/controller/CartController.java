package com.ll.exam.FinalProject_KimArum.app.cart.controller;

import com.ll.exam.FinalProject_KimArum.app.cart.entity.CartItem;
import com.ll.exam.FinalProject_KimArum.app.cart.service.CartService;
import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.product.entity.Product;
import com.ll.exam.FinalProject_KimArum.app.product.service.ProductService;
import com.ll.exam.FinalProject_KimArum.app.secutiry.dto.MemberContext;
import com.ll.exam.FinalProject_KimArum.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final ProductService productService;

    @GetMapping("/list")
    @PreAuthorize("isAuthenticated()")
    public String showItems(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        Member buyer = memberContext.getMember();

        List<CartItem> items = cartService.getItemsByBuyer(buyer);

        model.addAttribute("items", items);

        return "cart/list";
    }

    @PostMapping("/removeItems")
    @PreAuthorize("isAuthenticated()")
    public String removeItems(@AuthenticationPrincipal MemberContext memberContext, String ids) {
        Member buyer = memberContext.getMember();

        String[] idsArr = ids.split(",");

        Arrays.stream(idsArr)
                .mapToLong(Long::parseLong)
                .forEach(id -> {
                    CartItem cartItem = cartService.findItemById(id).orElse(null);

                    if (cartService.actorCanDelete(buyer, cartItem)) {
                        cartService.removeItem(cartItem);
                    }
                });

        return "redirect:/cart/list?msg=" + Util.url.encode("%d건의 품목을 삭제하였습니다.".formatted(idsArr.length));
    }

    @PostMapping("/add/{id}")
    @PreAuthorize("isAuthenticated()")
    public String addItem(@AuthenticationPrincipal MemberContext memberContext, @PathVariable Long id, HttpServletRequest request) {
        Member buyer = memberContext.getMember();
        Product product = productService.getProductById(id);

        if(cartService.hasItem(buyer, product)){
            return "redirect:"+request.getHeader("referer")+"?errorMsg=" + Util.url.encode("이미 장바구니에 담은 상품입니다.");
        }

        cartService.addItem(buyer, product);

        return "redirect:"+request.getHeader("referer")+"?msg=" + Util.url.encode("상품을 장바구니에 추가했습니다.");
    }
}
