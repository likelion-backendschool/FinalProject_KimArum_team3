package com.ll.exam.FinalProject_KimArum.app.post.repository;

import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findAllByOrderByIdDesc();
}
