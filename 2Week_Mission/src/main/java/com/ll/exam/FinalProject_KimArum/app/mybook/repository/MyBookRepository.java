package com.ll.exam.FinalProject_KimArum.app.mybook.repository;

import com.ll.exam.FinalProject_KimArum.app.mybook.entity.MyBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MyBookRepository extends JpaRepository<MyBook, Long> {
    MyBook findByBuyerIdAndProductId(long buyerId, long bookId);
}
