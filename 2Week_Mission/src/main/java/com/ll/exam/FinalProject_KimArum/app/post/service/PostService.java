package com.ll.exam.FinalProject_KimArum.app.post.service;

import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;
import com.ll.exam.FinalProject_KimArum.app.post.entity.PostHashTag;
import com.ll.exam.FinalProject_KimArum.app.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final PostHashTagService postHashTagService;

    public Post writePost(Long authorId, String subject, String content, String htmlContent) {
        return writePost(authorId, subject, content, htmlContent, "");
    }

    public Post writePost(Long authorId, String subject, String content, String htmlContent, String hashTagContents) {
        Post post = Post.builder()
                .author(new Member(authorId))
                .subject(subject)
                .content(content)
                .contentHtml(htmlContent)
                .build();

        postRepository.save(post);

        postHashTagService.applyHashTags(post, hashTagContents);

        return post;
    }

    @Transactional(readOnly = true)
    public Optional<Post> findBySubject(String subject) {
        return postRepository.findPostBySubject(subject);
    }

    public String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    public Post getPostById(Long id) {
        Post post = postRepository.findPostById(id).orElse(null);

        loadForPrintData(post);

        return post;
    }

    public List<Post> getPosts() {
        return postRepository.findAllByOrderByIdDesc();
    }

    public List<Post> getRecentPosts() {
        return postRepository.findRecentByOrderByIdDesc();
    }

    public void delete(long id) {
        Post post = postRepository.findPostById(id).orElse(null);
        postRepository.delete(post);
    }

    public void modify(Post post, String subject, String content, String htmlContent, String hashTagContents) {
        post.setSubject(subject);
        post.setContent(content);
        post.setContentHtml(htmlContent);

        postRepository.save(post);
        postHashTagService.applyHashTags(post, hashTagContents);
    }

    public List<Post> search(String kw) {
        return postRepository.searchQsl(kw);
    }

    public List<Post> search(Member author, String kw) {
        return postRepository.searchQslByAuthorAndKeyword(author, kw);
    }

    public void loadForPrintData(Post post) {
        List<PostHashTag> hashTags = postHashTagService.getHashTags(post);
        post.getExtra().put("hashTags", hashTags);
    }

    public void loadForPrintData(List<Post> posts) {
        long[] ids = posts
                .stream()
                .mapToLong(Post::getId)
                .toArray();

        List<PostHashTag> hashTagsByArticleIds = postHashTagService.getHashTagsByArticleIdIn(ids);

        Map<Long, List<PostHashTag>> hashTagsByArticleIdsMap = hashTagsByArticleIds.stream()
                .collect(groupingBy(
                        hashTag -> hashTag.getPost().getId(), toList()
                ));

        posts.stream().forEach(article -> {
            List<PostHashTag> hashTags = hashTagsByArticleIdsMap.get(article.getId());

            if (hashTags == null || hashTags.size() == 0) return;

            article.getExtra().put("hashTags", hashTags);
        });

        log.debug("posts : " + posts);
    }
}
