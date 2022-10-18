package com.ll.exam.FinalProject_KimArum;

import com.ll.exam.FinalProject_KimArum.app.post.controller.PostController;
import com.ll.exam.FinalProject_KimArum.app.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class PostTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private PostService postService;

    @Test
    @DisplayName("게시글 등록")
    @WithUserDetails("user1")
    void t1() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/post/write")
                        .with(csrf())
                        .param("subject", "hello")
                        .param("content", "hello world")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(PostController.class))
                .andExpect(handler().methodName("writePost"))
                .andExpect(redirectedUrl("/"));

        assertThat(postService.findBySubject("hello").isPresent()).isTrue();
    }
}
