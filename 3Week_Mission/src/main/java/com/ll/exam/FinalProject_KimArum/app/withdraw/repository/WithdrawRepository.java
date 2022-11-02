package com.ll.exam.FinalProject_KimArum.app.withdraw.repository;

import com.ll.exam.FinalProject_KimArum.app.withdraw.entity.Withdraw;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawRepository extends JpaRepository<Withdraw, Long> {
}