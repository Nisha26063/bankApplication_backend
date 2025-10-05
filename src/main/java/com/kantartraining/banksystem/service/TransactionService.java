package com.kantartraining.banksystem.service;

import com.kantartraining.banksystem.dto.TransactionDTO;
import com.kantartraining.banksystem.entity.Transaction;
import com.kantartraining.banksystem.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<TransactionDTO> getAccountTransactions(Long accountNo) {
        log.info("Fetching transactions for account: {}", accountNo);
        return transactionRepository.findByAccountAccountNoOrderByTransactionDateDesc(accountNo)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> getCustomerTransactions(Long customerId) {
        log.info("Fetching transactions for customer ID: {}", customerId);
        return transactionRepository.findByAccountCustomerIdOrderByTransactionDateDesc(customerId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getTransactionType(),
                transaction.getAmount(),
                transaction.getTransactionDate(),
                transaction.getAccount().getAccountNo(),
                transaction.getBalance()
        );
    }
}