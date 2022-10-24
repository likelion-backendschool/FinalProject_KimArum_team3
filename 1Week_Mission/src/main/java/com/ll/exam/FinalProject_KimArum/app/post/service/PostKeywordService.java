package com.ll.exam.FinalProject_KimArum.app.post.service;

import com.ll.exam.FinalProject_KimArum.app.post.entity.PostKeyword;
import com.ll.exam.FinalProject_KimArum.app.post.repository.PostKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostKeywordService {
    private final PostKeywordRepository postKeywordRepository;

    public PostKeyword save(String keywordContent) {
        Optional<PostKeyword> optKeyword = postKeywordRepository.findByContent(keywordContent);

        if ( optKeyword.isPresent() ) {
            return optKeyword.get();
        }

        PostKeyword keyword = PostKeyword
                .builder()
                .content(keywordContent)
                .build();

        postKeywordRepository.save(keyword);

        return keyword;
    }

    public List<PostKeyword> findAllByMemberId(Long id) {
        return postKeywordRepository.findAllByMemberId(id);
    }

    public PostKeyword findByContentOrSave(String content) {
        return save(content);
    }

    public Optional<PostKeyword> findById(long id) {
        return postKeywordRepository.findById(id);
    }
}
