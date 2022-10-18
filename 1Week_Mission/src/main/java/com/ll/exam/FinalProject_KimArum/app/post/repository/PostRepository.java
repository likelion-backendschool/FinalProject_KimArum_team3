package com.ll.exam.FinalProject_KimArum.app.post.repository;

import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    Optional<Post> findPostBySubject(String subject);

    Optional<Post> findPostById(Long id);
}
