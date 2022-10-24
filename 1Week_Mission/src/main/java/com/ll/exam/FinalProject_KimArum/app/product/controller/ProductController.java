package com.ll.exam.FinalProject_KimArum.app.product.controller;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;
import com.ll.exam.FinalProject_KimArum.app.post.entity.PostKeyword;
import com.ll.exam.FinalProject_KimArum.app.post.service.PostKeywordService;
import com.ll.exam.FinalProject_KimArum.app.product.entity.Product;
import com.ll.exam.FinalProject_KimArum.app.product.form.ProductForm;
import com.ll.exam.FinalProject_KimArum.app.product.service.ProductService;
import com.ll.exam.FinalProject_KimArum.app.secutiry.dto.MemberContext;
import com.ll.exam.FinalProject_KimArum.base.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
        Product product = productService.findForPrintById(id).get();
        List<Post> posts = productService.findPostsByProduct(product);

        model.addAttribute("product", product);
        model.addAttribute("posts", posts);

        return "product/detail";
    }

    @GetMapping("/list")
    public String showList(Model model, @RequestParam(required = false) String kwType, @RequestParam(required = false) String kw) {
        List<Product> products = productService.search(kwType, kw);

        productService.loadForPrintData(products);

        System.out.println(products);

        model.addAttribute("products", products);


        return "product/list";
    }
}
