package com.ll.exam.FinalProject_KimArum.app.post.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class PostForm {
    @NotEmpty
    @Size(min=1, max = 20, message = "제목을 20자 이하로 입력해주세요.")
    private String subject;
    @NotEmpty
    @Size(min=1, max = 2000, message = "내용을 2000자 이하로 입력해주세요.")
    private String content;
    private String hashTagContents;
}
