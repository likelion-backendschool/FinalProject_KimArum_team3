package com.ll.exam.FinalProject_KimArum.app.product.dto;

import com.ll.exam.FinalProject_KimArum.app.post.dto.PostDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    Long id;
    LocalDateTime createDate;
    LocalDateTime modifyDate;
    Long authorId;
    String authorName;
    String subject;
    List<PostDto> bookChapters;
}
