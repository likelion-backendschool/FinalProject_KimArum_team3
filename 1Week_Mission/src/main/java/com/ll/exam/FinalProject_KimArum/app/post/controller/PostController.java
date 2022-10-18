package com.ll.exam.FinalProject_KimArum.app.post.controller;

import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;
import com.ll.exam.FinalProject_KimArum.app.post.form.PostForm;
import com.ll.exam.FinalProject_KimArum.app.post.service.PostService;
import com.ll.exam.FinalProject_KimArum.app.secutiry.dto.MemberContext;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String showWritePost() {
        return "post/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String writePost(@AuthenticationPrincipal MemberContext memberContext, @Valid PostForm postForm) {
        String htmlContent = postService.markdownToHtml(postForm.getContent());
        Post post = postService.writePost(memberContext.getId(), postForm.getSubject(), postForm.getContent(), htmlContent);

        return "redirect:/";
    }

    @GetMapping("/{id}")
    public String showDetail(Model model, @PathVariable Long id) {
        Post post = postService.getPostById(id);

        model.addAttribute("post", post);

        return "post/detail";
    }
}
