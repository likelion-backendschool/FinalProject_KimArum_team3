package com.ll.exam.FinalProject_KimArum.app.post.repository;

import com.ll.exam.FinalProject_KimArum.app.post.entity.PostKeyword;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

import static com.ll.exam.FinalProject_KimArum.app.post.entity.QPostHashTag.postHashTag;
import static com.ll.exam.FinalProject_KimArum.app.post.entity.QPostKeyword.postKeyword;

@RequiredArgsConstructor
public class PostKeywordRepositoryImpl implements PostKeywordRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostKeyword> findAllByMemberId(Long id) {
        List<Tuple> fetch = jpaQueryFactory
                .select(postKeyword, postHashTag.count())
                .from(postKeyword)
                .innerJoin(postHashTag)
                .on(postKeyword.eq(postHashTag.keyword))
                .where(postHashTag.member.id.eq(id))
                .orderBy(postHashTag.post.id.desc())
                .groupBy(postKeyword.id)
                .fetch();

        return fetch.stream().
                map(tuple -> {
                    PostKeyword _postKeyword =tuple.get(postKeyword);
                    Long postHashTagsCount = tuple.get(postHashTag.count());

                    _postKeyword.getExtra().put("postHashTagsCount", postHashTagsCount);

                    return _postKeyword;
                })
                .collect(Collectors.toList());
    }
}
