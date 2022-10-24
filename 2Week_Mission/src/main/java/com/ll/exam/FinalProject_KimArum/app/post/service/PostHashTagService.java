package com.ll.exam.FinalProject_KimArum.app.post.service;

import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;
import com.ll.exam.FinalProject_KimArum.app.post.entity.PostHashTag;
import com.ll.exam.FinalProject_KimArum.app.post.entity.PostKeyword;
import com.ll.exam.FinalProject_KimArum.app.post.repository.PostHashTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostHashTagService {
    private final PostHashTagRepository postHashTagRepository;
    private final PostKeywordService postKeywordService;

    public void applyHashTags(Post post, String hashTagContents) {
        List<PostHashTag> oldHashTags = getHashTags(post);

        List<String> keywordContents = Arrays.stream(hashTagContents.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        List<PostHashTag> needToDelete = new ArrayList<>();

        for (PostHashTag oldHashTag : oldHashTags) {
            boolean contains = keywordContents.stream().anyMatch(s -> s.equals(oldHashTag.getKeyword().getContent()));

            if (contains == false) {
                needToDelete.add(oldHashTag);
            }
        }

        needToDelete.forEach(hashTag -> {
            postHashTagRepository.delete(hashTag);
        });

        keywordContents.forEach(keywordContent -> {
            savePostHashTag(post, keywordContent);
        });
    }

    private PostHashTag savePostHashTag(Post post, String keywordContent) {
        PostKeyword keyword = postKeywordService.save(keywordContent);

        Optional<PostHashTag> opHashTag = postHashTagRepository.findByPostIdAndKeywordId(post.getId(), keyword.getId());

        if (opHashTag.isPresent()) {
            return opHashTag.get();
        }

        PostHashTag hashTag = PostHashTag.builder()
                .post(post)
                .member(post.getAuthor())
                .keyword(keyword)
                .build();

        postHashTagRepository.save(hashTag);

        return hashTag;
    }

    public List<PostHashTag> getHashTags(Post post) {
        return postHashTagRepository.findAllByPostId(post.getId());
    }

    public List<PostHashTag> getHashTagsByArticleIdIn(long[] ids) {
        return postHashTagRepository.findAllByPostIdIn(ids);
    }

    public List<PostHashTag> getPostTags(long authorId, long postKeywordId) {
        return postHashTagRepository.findAllByMemberIdAndKeywordIdOrderByPost_idDesc(authorId, postKeywordId);
    }
}
