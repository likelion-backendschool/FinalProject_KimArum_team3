package com.ll.exam.FinalProject_KimArum.app.product.controller;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;
import com.ll.exam.FinalProject_KimArum.app.post.entity.PostKeyword;
import com.ll.exam.FinalProject_KimArum.app.post.service.PostKeywordService;
import com.ll.exam.FinalProject_KimArum.app.product.entity.Product;
import com.ll.exam.FinalProject_KimArum.app.product.form.ProductForm;
import com.ll.exam.FinalProject_KimArum.app.product.form.ProductModifyForm;
import com.ll.exam.FinalProject_KimArum.app.product.service.ProductService;
import com.ll.exam.FinalProject_KimArum.app.secutiry.dto.MemberContext;
import com.ll.exam.FinalProject_KimArum.base.Rq;
import com.ll.exam.FinalProject_KimArum.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final PostKeywordService postKeywordService;
    private final Rq rq;

    @GetMapping("/create")
    public String showCreate(@AuthenticationPrincipal MemberContext memberContext, Model model) {
        System.out.println(memberContext.getId());
        List<PostKeyword> keywords = postKeywordService.findAllByMemberId(memberContext.getId());

        model.addAttribute("keywords", keywords);

        return "product/create";
    }

    @PreAuthorize("isAuthenticated() and hasAuthority('ADMIN')")
    @PostMapping("/create")
    public String create(@Valid ProductForm productForm){
        Member member = rq.getMember();
        Product product = productService.create(member, productForm.getSubject(), productForm.getPrice(), productForm.getPostKeywordId(), productForm.getProductTagContents());
        return "redirect:/product/" + product.getId();
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        List<Post> posts = productService.findPostsByProduct(product);

        model.addAttribute("product", product);
        model.addAttribute("posts", posts);

        return "product/detail";
    }

    @GetMapping("/list")
    public String showList(Model model, @RequestParam(required = false) String kwType, @RequestParam(required = false) String kw) {
        List<Product> products = productService.search(kwType, kw);

        productService.loadForPrintData(products);

        model.addAttribute("products", products);

        return "product/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/delete")
    public String deletePost(@AuthenticationPrincipal MemberContext memberContext, @PathVariable Long id) {
        Product product = productService.getProductById(id);

        if(product == null) {
            return "redirect:/product/"+id+"?errorMsg=" + Util.url.encode("해당 상품이 존재하지 않습니다.");
        }

        //Member member = memberService.findByUsername(memberContext.getUsername()).get();

        if (memberContext.memberIsNot(product.getAuthor())) {
            return "redirect:/product/"+id+"?errorMsg=" + Util.url.encode("본인이 등록하지 않은 상품입니다.");
        }

        productService.delete(id);

        return "redirect:/product/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(@AuthenticationPrincipal MemberContext memberContext, Model model, @PathVariable Long id, ProductModifyForm ProductModifyForm) {
        Product product = productService.getProductById(id);

        if(product == null) {
            return "redirect:/product/"+id+"?errorMsg=" + Util.url.encode("해당 상품이 존재하지 않습니다.");
        }

        if (memberContext.memberIsNot(product.getAuthor())) {
            return "redirect:/product/"+id+"?errorMsg=" + Util.url.encode("본인이 등록하지 않은 상품입니다.");
        }

        model.addAttribute("product", product);

        return "product/modify";
    }

    @PostMapping("/{id}/modify")
    public String modify(@AuthenticationPrincipal MemberContext memberContext, Model model, @PathVariable Long id, @Valid ProductModifyForm productModifyForm, BindingResult bindingResult) {
        Product product = productService.getProductById(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("product", product);
            return "product/modify";
        }

        if (memberContext.memberIsNot(product.getAuthor())) {
            return "redirect:/product/"+id+"?errorMsg=" + Util.url.encode("본인이 등록하지 않은 상품입니다.");
        }

        productService.modify(product, productModifyForm.getSubject(), productModifyForm.getPrice(), productModifyForm.getProductTagContents());

        return "redirect:/product/"+id+"?msg=" + Util.url.encode("상품이 수정되었습니다.");
    }
}
