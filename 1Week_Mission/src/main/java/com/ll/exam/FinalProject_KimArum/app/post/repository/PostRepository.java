package com.ll.exam.FinalProject_KimArum.app.post.repository;

import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findBySubject(String subject);
}
