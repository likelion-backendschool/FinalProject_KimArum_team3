package com.ll.exam.FinalProject_KimArum.app.member.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ModifyPasswordForm {
    @NotEmpty
    private String oldPassword;
    @NotEmpty
    private String newPassword;
    @NotEmpty
    private String newPasswordConfirm;
}
