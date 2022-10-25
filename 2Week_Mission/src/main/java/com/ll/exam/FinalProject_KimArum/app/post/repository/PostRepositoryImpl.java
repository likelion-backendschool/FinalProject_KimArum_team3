package com.ll.exam.FinalProject_KimArum.app.post.repository;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;
import com.ll.exam.FinalProject_KimArum.util.Util;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ll.exam.FinalProject_KimArum.app.post.entity.QPost.post;
import static com.ll.exam.FinalProject_KimArum.app.post.entity.QPostHashTag.postHashTag;
import static com.ll.exam.FinalProject_KimArum.app.post.entity.QPostKeyword.postKeyword;

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

    @Override
    public List<Post> findRecentByOrderByIdDesc() {
        return jpaQueryFactory
                .select(post)
                .from(post)
                .orderBy(post.id.desc())
                .limit(100)
                .fetch();
    }

    @Override
    public List<Post> searchQsl(String kw) {
        // 키워드가 없거나, 검색타입이 hashTag가 아닌경우, 전체 게시물
        JPAQuery<Post> jpqQuery = jpaQueryFactory
                .select(post)
                .distinct()
                .from(post);

        // 키워드가 존재하고
        if (Util.str.empty(kw) == false) {
            jpqQuery
                    .innerJoin(postHashTag)
                    .on(post.eq(postHashTag.post))
                    .innerJoin(postKeyword)
                    .on(postKeyword.eq(postHashTag.keyword))
                    .where(postKeyword.content.eq(kw));


        }

        jpqQuery.orderBy(post.id.desc());

        return jpqQuery.fetch();
    }

    @Override
    public List<Post> searchQslByAuthorAndKeyword(Member author, String kw) {
        // 키워드가 없거나, 검색타입이 hashTag가 아닌경우, 전체 게시물
        JPAQuery<Post> jpqQuery = jpaQueryFactory
                .select(post)
                .distinct()
                .from(post)
                .where(post.author.eq(author));

        // 키워드가 존재하고
        if (Util.str.empty(kw) == false) {
            jpqQuery
                    .innerJoin(postHashTag)
                    .on(post.eq(postHashTag.post))
                    .innerJoin(postKeyword)
                    .on(postKeyword.eq(postHashTag.keyword))
                    .where(postKeyword.content.eq(kw));

        }

        jpqQuery.orderBy(post.id.desc());

        return jpqQuery.fetch();
    }
}
