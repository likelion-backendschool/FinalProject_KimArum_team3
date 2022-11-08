package com.ll.exam.FinalProject_KimArum.app.myBook.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ll.exam.FinalProject_KimArum.app.base.entity.BaseEntity;
import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.order.entity.OrderItem;
import com.ll.exam.FinalProject_KimArum.app.product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class MyBook extends BaseEntity {
    @ManyToOne
    @ToString.Exclude
    private Member owner;

    @ManyToOne
    @ToString.Exclude
    private Product product;

    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    @JsonIgnore
    private OrderItem orderItem;
}
