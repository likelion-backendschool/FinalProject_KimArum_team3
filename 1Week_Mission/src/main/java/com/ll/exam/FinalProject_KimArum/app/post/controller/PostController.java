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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public String showWritePost(PostForm postForm) {
        return "post/write";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/write")
    public String writePost(@AuthenticationPrincipal MemberContext memberContext, @Valid PostForm postForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "post/write";
        }

        Post post = postService.writePost(memberContext.getId(), postForm.getSubject(), postForm.getContent(), postForm.getContentHtml(), postForm.getHashTagContents());

        return "redirect:/post/list";
    }

    @GetMapping("/{id}")
    public String showDetail(Model model, @PathVariable Long id) {
        Post post = postService.getPostById(id);

        model.addAttribute("post", post);

        return "post/detail";
    }

    @GetMapping("/list")
    public String showList(Model model, @RequestParam(required = false) String kwType, @RequestParam(required = false) String kw) {
        List<Post> posts = postService.search(kwType, kw);

        postService.loadForPrintData(posts);

        model.addAttribute("posts", posts);


        return "post/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/delete")
    public String deletePost(@AuthenticationPrincipal MemberContext memberContext, @PathVariable Long id) {
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
    public String showModify(@AuthenticationPrincipal MemberContext memberContext, Model model, @PathVariable Long id, PostForm postForm) {
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
    public String modify(@AuthenticationPrincipal MemberContext memberContext, Model model, @PathVariable Long id, @Valid PostForm postForm, BindingResult bindingResult) {
        Post post = postService.getPostById(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "post/modify";
        }

        if (memberContext.memberIsNot(post.getAuthor())) {
            return "redirect:/post/"+id+"?errorMsg=" + Util.url.encode("본인이 작성하지 않은 글입니다.");
        }

        postService.modify(post, postForm.getSubject(), postForm.getContent(), postForm.getContentHtml(), postForm.getHashTagContents());

        return "redirect:/post/"+id+"?msg=" + Util.url.encode("게시글이 수정되었습니다.");
    }
}
