package com.ll.exam.FinalProject_KimArum.app.member.form;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotEmpty;

@Data
public class ModifyForm {
    @NotEmpty
    private String username;
    @NotEmpty
    private String email;
    @Nullable
    private String nickname;
}
