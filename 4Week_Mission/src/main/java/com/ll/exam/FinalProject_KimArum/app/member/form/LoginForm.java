package com.ll.exam.FinalProject_KimArum.app.member.form;

import lombok.Data;

@Data
public class LoginForm {
    private String username;
    private String password;

    public boolean isNotValid() {
        return username == null || password == null || username.trim().length() == 0 || password.trim().length() == 0;
    }
}
