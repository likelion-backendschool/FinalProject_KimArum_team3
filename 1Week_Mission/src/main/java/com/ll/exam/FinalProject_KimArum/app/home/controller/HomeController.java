package com.ll.exam.FinalProject_KimArum.app.home.controller;

import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;
import com.ll.exam.FinalProject_KimArum.app.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final PostService postService;

    @GetMapping("/")
    public String showMain(Model model) {
        List<Post> posts = postService.getRecentPosts();
        model.addAttribute("posts", posts);

        return "/post/list";
    }
}
