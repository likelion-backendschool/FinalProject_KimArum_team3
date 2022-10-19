package com.ll.exam.FinalProject_KimArum.app.post.controller;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.member.service.MemberService;
import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;
import com.ll.exam.FinalProject_KimArum.app.post.form.PostForm;
import com.ll.exam.FinalProject_KimArum.app.post.service.PostService;
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

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/write")
    public String showWritePost() {
        return "post/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String writePost(@AuthenticationPrincipal MemberContext memberContext, @Valid PostForm postForm) {
        String htmlContent = postService.markdownToHtml(postForm.getContent());
        Post post = postService.writePost(memberContext.getId(), postForm.getSubject(), postForm.getContent(), htmlContent, postForm.getHashTagContents());

        return "redirect:/post/list";
    }

    @GetMapping("/{id}")
    public String showDetail(Model model, @PathVariable Long id) {
        Post post = postService.getPostById(id);

        model.addAttribute("post", post);

        return "post/detail";
    }

    @GetMapping("/list")
    public String showList(Model model) {
        List<Post> posts = postService.getPosts();
        model.addAttribute("posts", posts);

        return "post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/delete")
    public String deleteBoard(@AuthenticationPrincipal MemberContext memberContext, @PathVariable Long id) {
        Post post = postService.getPostById(id);

        if(post == null) {
            return "redirect:/post/"+id+"?errorMsg=" + Util.url.encode("해당 게시글이 존재하지 않습니다.");
        }

        Member member = memberService.findByUsername(memberContext.getUsername()).get();

        if (memberContext.memberIsNot(post.getAuthor())) {
            return "redirect:/post/"+id+"?errorMsg=" + Util.url.encode("본인이 작성하지 않은 글입니다.");
        }

        postService.delete(id);

        return "redirect:/post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/modify")
    public String showModify(@AuthenticationPrincipal MemberContext memberContext, Model model, @PathVariable Long id) {
        Post post = postService.getPostById(id);

        if(post == null) {
            return "redirect:/post/"+id+"?errorMsg=" + Util.url.encode("해당 게시글이 존재하지 않습니다.");
        }

        if (memberContext.memberIsNot(post.getAuthor())) {
            return "redirect:/post/"+id+"?errorMsg=" + Util.url.encode("본인이 작성하지 않은 글입니다.");
        }

        model.addAttribute("post", post);

        return "post/modify";
    }

    @PostMapping("/{id}/modify")
    public String modify(@AuthenticationPrincipal MemberContext memberContext, Model model, @PathVariable Long id, @Valid PostForm postForm) {
        Post post = postService.getPostById(id);

        if (memberContext.memberIsNot(post.getAuthor())) {
            return "redirect:/post/"+id+"?errorMsg=" + Util.url.encode("본인이 작성하지 않은 글입니다.");
        }

        String htmlContent = postService.markdownToHtml(postForm.getContent());
        postService.modify(post, postForm.getSubject(), postForm.getContent(), htmlContent);

        return "redirect:/post/"+id+"?msg=" + Util.url.encode("게시글이 수정되었습니다.");
    }
}
