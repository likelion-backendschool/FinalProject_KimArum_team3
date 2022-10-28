package com.ll.exam.FinalProject_KimArum.app.myBook.repository;

import com.ll.exam.FinalProject_KimArum.app.myBook.entity.MyBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyBookRepository extends JpaRepository<MyBook, Long> {
    void deleteAllByProductIdAndOwnerId(long productId, long ownerId);
}
