package com.ll.exam.FinalProject_KimArum;

import com.ll.exam.FinalProject_KimArum.app.member.controller.MemberController;
import com.ll.exam.FinalProject_KimArum.app.member.service.MemberService;
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
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class MemberTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원가입 폼")
    void t1() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/member/join"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("showJoin"))
                .andExpect(content().string(containsString("회원가입")));
    }

    @Test
    @DisplayName("회원가입 - 닉네임 입력")
    void t2() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/member/join")
                        .with(csrf())
                        .param("username", "user999")
                        .param("password", "1234")
                        .param("passwordConfirm", "1234")
                        .param("email", "user999@test.com")
                        .param("nickname", "hamster1")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(redirectedUrlPattern("/member/login?msg=**"));

        assertThat(memberService.findByUsername("user999").isPresent()).isTrue();
    }

    @Test
    @DisplayName("회원가입 - 닉네임 미입력")
    void t3() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/member/join")
                        .with(csrf())
                        .param("username", "user999")
                        .param("password", "1234")
                        .param("passwordConfirm", "1234")
                        .param("email", "user999@test.com")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("join"))
                .andExpect(redirectedUrlPattern("/member/login?msg=**"));

        assertThat(memberService.findByUsername("user999").isPresent()).isTrue();
    }

    @Test
    @DisplayName("회원정보 수정 폼")
    @WithUserDetails("user1")
    void t4() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(get("/member/modify"))
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is2xxSuccessful())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("showModify"))
                .andExpect(content().string(containsString("회원 정보 수정")));
    }

    @Test
    @DisplayName("회원정보수정 후 프로필페이지 이동, 회원정보 보여야 함")
    @WithUserDetails("user1")
    void t5() throws Exception {
        // WHEN
        ResultActions resultActions = mvc
                .perform(post("/member/modify")
                        .with(csrf())
                        .param("username", "user1")
                        .param("email", "user1@test2.com")
                        .param("nickname", "햄스터")
                )
                .andDo(print());

        // THEN
        resultActions
                .andExpect(status().is3xxRedirection())
                .andExpect(handler().handlerType(MemberController.class))
                .andExpect(handler().methodName("modify"))
                .andExpect(redirectedUrl("/member/profile"))
                .andExpect(content().string("user1@test2.com"))
                .andExpect(content().string("햄스터"));

        assertThat(memberService.findByUsername("user1").isPresent()).isTrue();
    }
}
