package com.ll.exam.FinalProject_KimArum.app.myBook.controller;

import com.ll.exam.FinalProject_KimArum.app.base.dto.RsData;
import com.ll.exam.FinalProject_KimArum.app.base.rq.Rq;
import com.ll.exam.FinalProject_KimArum.app.member.entity.Member;
import com.ll.exam.FinalProject_KimArum.app.myBook.entity.MyBook;
import com.ll.exam.FinalProject_KimArum.app.myBook.service.MyBookService;
import com.ll.exam.FinalProject_KimArum.app.security.dto.MemberContext;
import com.ll.exam.FinalProject_KimArum.util.Ut;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/myBooks")
@Tag(name = "MemberController", description = "회원이 구매한 도서 리스트, 상세 정보 제공 기능을 담당하는 컨트롤러")
public class MyBookController {
    private final MyBookService myBookService;
    private final Rq rq;

    @GetMapping("")
    public ResponseEntity<RsData> list(@AuthenticationPrincipal MemberContext memberContext){
        Member member = memberContext.getMember();
        List<MyBook> myBooks = myBookService.findAllByOwnerId(member.getId());

        return Ut.spring.responseEntityOf(
                RsData.successOf(
                        Ut.mapOf(
                                "myBooks", myBooks
                        )
                )
        );
    }
}