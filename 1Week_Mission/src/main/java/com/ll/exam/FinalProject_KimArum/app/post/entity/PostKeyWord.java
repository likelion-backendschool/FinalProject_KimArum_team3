package com.ll.exam.FinalProject_KimArum.app.post.entity;

import com.ll.exam.FinalProject_KimArum.base.entity.BaseEntity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class PostKeyword extends BaseEntity {
    private String content;

    public String getListUrl() {
        return "/post/list?kwType=keyword&kw=%s".formatted(content);
    }
}
