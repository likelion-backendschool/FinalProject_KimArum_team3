package com.ll.exam.FinalProject_KimArum.app.post.service;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;
import com.ll.exam.FinalProject_KimArum.app.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public Post writePost(Long authorId, String subject, String content) {
        Post post = Post.builder()
                .author(new Member(authorId))
                .subject(subject)
                .content(content)
                .build();

        postRepository.save(post);

        return post;
    }

    @Transactional(readOnly = true)
    public Optional<Post> findBySubject(String subject) {
        return postRepository.findBySubject(subject);
    }
}
