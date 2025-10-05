package com.kantartraining.banksystem.repository;

import com.kantartraining.banksystem.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Find transactions by account number (ordered by date descending)
    List<Transaction> findByAccountAccountNoOrderByTransactionDateDesc(Long accountNo);

    // Find transactions by customer ID (ordered by date descending)
    List<Transaction> findByAccountCustomerIdOrderByTransactionDateDesc(Long customerId);

}