package com.ll.exam.FinalProject_KimArum.app.post.repository;

import com.ll.exam.FinalProject_KimArum.app.post.entity.PostHashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostHashTagRepository extends JpaRepository<PostHashTag, Long> {
    Optional<PostHashTag> findByPostIdAndKeywordId(Long postId, Long keywordId);

    List<PostHashTag> findAllByPostId(Long postId);
}
