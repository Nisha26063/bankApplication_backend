package com.kantartraining.banksystem.repository;

import com.kantartraining.banksystem.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNo(Long accountNo);

    List<Account> findByCustomerId(Long customerId);

    @Query("SELECT MAX(a.accountNo) FROM Account a")
    Long findMaxAccountNumber();

    @Modifying
    @Query("UPDATE Account a SET a.balance = :newBalance WHERE a.accountNo = :accountNo")
    void updateBalance(@Param("accountNo") Long accountNo, @Param("newBalance") double newBalance);
}