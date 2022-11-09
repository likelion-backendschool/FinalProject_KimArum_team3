package com.ll.exam.FinalProject_KimArum.app.post.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostDto {
    Long id;
    String subject;
    String content;
    String contentHtml;
}
