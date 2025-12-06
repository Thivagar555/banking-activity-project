package com.banking.BankingProject.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountId(Long accountId);

    @Query("SELECT COUNT(t) FROM Transaction t WHERE t.account.id = :accountId AND t.time >= :time")
    long countRecentTransfers(Long accountId, LocalDateTime time);
}
