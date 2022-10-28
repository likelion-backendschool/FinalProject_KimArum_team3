package com.ll.exam.FinalProject_KimArum.app.post.repository;


import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByAuthorIdOrderByIdDesc(long authorId);
}
