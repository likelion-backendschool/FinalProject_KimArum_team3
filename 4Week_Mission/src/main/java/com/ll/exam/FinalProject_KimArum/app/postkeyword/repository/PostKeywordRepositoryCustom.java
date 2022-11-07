package com.ll.exam.FinalProject_KimArum.app.postkeyword.repository;

import com.ll.exam.FinalProject_KimArum.app.postkeyword.entity.PostKeyword;

import java.util.List;

public interface PostKeywordRepositoryCustom {
    List<PostKeyword> getQslAllByAuthorId(Long authorId);
}
