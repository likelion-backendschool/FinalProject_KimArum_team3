package com.ll.exam.FinalProject_KimArum;

import com.ll.exam.FinalProject_KimArum.app.post.controller.PostController;
import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;
import com.ll.exam.FinalProject_KimArum.app.post.entity.PostHashTag;
import com.ll.exam.FinalProject_KimArum.app.post.service.PostHashTagService;
import com.ll.exam.FinalProject_KimArum.app.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Autowired
    private PostHashTagService postHashTagService;

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

    @Test
    @DisplayName("1번 게시물에는 해시태그가 2개 존재한다.")
    void t2() {
        Post post = postService.getPostById(1L);
        List<PostHashTag> hashTags = postHashTagService.getHashTags(post);

        assertThat(hashTags.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("1번 게시물의 해시태그를 수정하면, 기존 해시태그 중 몇개는 지워질 수 있다.")
    @Rollback(false)
    void t3() {
        String keywordContentsStr = "#태그1 #태그4";
        Post post = postService.getPostById(1L);
        postHashTagService.applyHashTags(post, keywordContentsStr);
    }

    @Test
    @DisplayName("해시태그 태그2와 관련된 모든 게시물 조회")
    void t4() {
        List<Post> articles = postService.search("hashTag", "자바");

        assertThat(articles.size()).isEqualTo(1);
    }
}
