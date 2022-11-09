package com.ll.exam.FinalProject_KimArum.app.myBook.dto;

import com.ll.exam.FinalProject_KimArum.app.myBook.entity.MyBook;
import com.ll.exam.FinalProject_KimArum.app.post.dto.PostDto;
import com.ll.exam.FinalProject_KimArum.app.post.entity.Post;
import com.ll.exam.FinalProject_KimArum.app.product.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyBookDto {

    Long id;
    LocalDateTime createDate;
    LocalDateTime modifyDate;
    Long ownerId;
    ProductDto product;

    public static List<MyBookDto> getApiMyBookByMyBook(List<MyBook> myBooks) {

        List<MyBookDto> myBookDtos = new ArrayList<>();

        for(MyBook myBook : myBooks){
            myBookDtos.add(MyBookDto.builder()
                    .id(myBook.getId())
                    .createDate(myBook.getCreateDate())
                    .modifyDate(myBook.getModifyDate())
                    .ownerId(myBook.getOwner().getId())
                    .product(ProductDto.builder()
                            .id(myBook.getProduct().getId())
                            .createDate(myBook.getProduct().getCreateDate())
                            .modifyDate(myBook.getProduct().getModifyDate())
                            .authorId(myBook.getProduct().getAuthor().getId())
                            .authorName(myBook.getProduct().getAuthor().getNickname())
                            .subject(myBook.getProduct().getSubject())
                            .build())
                    .build());
        }

        return myBookDtos;
    }

    public static MyBookDto getApiMyBookByMyBook(MyBook myBook, List<Post> posts) {

        List<PostDto> postDtos = new ArrayList<>();

        for(Post p : posts){
            postDtos.add(PostDto.builder()
                    .id(p.getId())
                    .subject(p.getSubject())
                    .content(p.getContent())
                    .contentHtml(p.getContentHtml())
                    .build());
        }


        MyBookDto apiMyBook = MyBookDto.builder()
                .id(myBook.getId())
                .createDate(myBook.getCreateDate())
                .modifyDate(myBook.getModifyDate())
                .ownerId(myBook.getOwner().getId())
                .product(ProductDto.builder()
                        .id(myBook.getProduct().getId())
                        .createDate(myBook.getProduct().getCreateDate())
                        .modifyDate(myBook.getProduct().getModifyDate())
                        .authorId(myBook.getProduct().getAuthor().getId())
                        .authorName(myBook.getProduct().getAuthor().getNickname())
                        .subject(myBook.getProduct().getSubject())
                        .bookChapters(postDtos)
                        .build())
                .build();

        return apiMyBook;
    }
}
