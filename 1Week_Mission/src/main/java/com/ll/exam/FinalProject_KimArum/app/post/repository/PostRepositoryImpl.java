package com.ll.exam.FinalProject_KimArum.app.post.repository;

import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ll.exam.FinalProject_KimArum.app.post.entity.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Post> findAllByOrderByIdDesc() {
        return jpaQueryFactory
                .select(post)
                .from(post)
                .orderBy(post.id.desc())
                .fetch();
    }
}
