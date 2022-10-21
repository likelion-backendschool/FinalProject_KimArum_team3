package com.ll.exam.FinalProject_KimArum.app.member.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class JoinForm {
    @NotEmpty
    @Size(min = 1, max = 10, message = "아이디는 1자 이상, 10자 이하로 설정 가능합니다.")
    private String username;
    @NotEmpty
    private String password;
    @NotEmpty
    private String passwordConfirm;
    @NotEmpty
    private String email;

}
