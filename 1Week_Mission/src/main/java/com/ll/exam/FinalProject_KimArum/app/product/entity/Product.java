package com.ll.exam.FinalProject_KimArum.app.product.entity;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.post.entity.PostKeyword;
import com.ll.exam.FinalProject_KimArum.base.entity.BaseEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Product extends BaseEntity {
    private String subject;
    private int price;
    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    private Member author;
    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    private PostKeyword postKeyword;

    public Product(long id) {
        super(id);
    }
}
