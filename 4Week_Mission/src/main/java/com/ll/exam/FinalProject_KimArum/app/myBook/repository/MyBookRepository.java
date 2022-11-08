package com.ll.exam.FinalProject_KimArum.app.myBook.repository;

import com.ll.exam.FinalProject_KimArum.app.myBook.entity.MyBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MyBookRepository extends JpaRepository<MyBook, Long> {
    void deleteAllByProductIdAndOwnerId(long productId, long ownerId);

    Optional<MyBook> findByOwnerIdAndProductId(long ownerId, long productId);

    List<MyBook> findAllByOwnerId(long ownerId);
}
