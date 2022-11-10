package com.ll.exam.FinalProject_KimArum.app.myBook.controller;

import com.ll.exam.FinalProject_KimArum.app.base.dto.RsData;
import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.myBook.dto.MyBookDto;
import com.ll.exam.FinalProject_KimArum.app.myBook.entity.MyBook;
import com.ll.exam.FinalProject_KimArum.app.myBook.service.MyBookService;
import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;
import com.ll.exam.FinalProject_KimArum.app.post.service.PostService;
import com.ll.exam.FinalProject_KimArum.app.postTag.entity.PostTag;
import com.ll.exam.FinalProject_KimArum.app.postTag.service.PostTagService;
import com.ll.exam.FinalProject_KimArum.app.security.dto.MemberContext;
import com.ll.exam.FinalProject_KimArum.util.Ut;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/myBooks")
@Tag(name = "MemberController", description = "회원이 구매한 도서 리스트, 상세 정보 제공 기능을 담당하는 컨트롤러")
public class MyBookController {
    private final MyBookService myBookService;
    private final PostTagService postTagService;
    private final PostService postService;

    @GetMapping("")
    public ResponseEntity<RsData> list(@AuthenticationPrincipal MemberContext memberContext){
        Member member = memberContext.getMember();
        List<MyBook> myBooks = myBookService.findAllByOwnerId(member.getId());

        List<MyBookDto> myBookDtos = MyBookDto.myBookToMyBookDto(myBooks);

        return Ut.spring.responseEntityOf(
                RsData.successOf(
                        Ut.mapOf(
                                "myBooks", myBookDtos
                        )
                )
        );
    }

    @GetMapping("/{myBookId}")
    public ResponseEntity<RsData> detail(@PathVariable long myBookId){
        MyBook myBook = myBookService.findById(myBookId).orElse(null);

        List<PostTag> postTags = postTagService.findByPostKeyword(myBook.getProduct().getPostKeyword());
        Member author = myBook.getProduct().getAuthor();

        List<Post> posts = new ArrayList<>();

        for(PostTag postTag : postTags){
            if(postTag.getMember().equals(author)){
                posts.add(postTag.getPost());
            }
        }

        MyBookDto myBookDto = MyBookDto.myBookToMyBookDto(myBook, posts);

        if(myBook == null){
            return Ut.spring.responseEntityOf(RsData.of("F-1", "도서 정보가 올바르지 않습니다."));
        }

        return Ut.spring.responseEntityOf(
                RsData.successOf(
                        Ut.mapOf(
                                "myBook", myBookDto
                        )
                )
        );
    }
}