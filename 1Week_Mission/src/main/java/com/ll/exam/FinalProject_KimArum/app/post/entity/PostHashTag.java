package com.ll.exam.FinalProject_KimArum.app.post.entity;

import com.ll.exam.FinalProject_KimArum.base.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class PostHashTag extends BaseEntity {
    @ManyToOne
    private Post post;
    @ManyToOne
    private PostKeyWord keyword;
}
