package com.kantartraining.banksystem.Service;

import com.kantartraining.banksystem.dto.TransactionDTO;
import com.kantartraining.banksystem.entity.Account;
import com.kantartraining.banksystem.entity.Transaction;
import com.kantartraining.banksystem.repository.TransactionRepository;
import com.kantartraining.banksystem.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    private Account account;
    private Transaction transaction;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        account = new Account();
        account.setAccountNo(1L);

        transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTransactionType("Deposit");
        transaction.setAmount(500.0);
        transaction.setTransactionDate(LocalDate.now());
    }

    @Test
    void testGetAccountTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        when(transactionRepository.findByAccountAccountNoOrderByTransactionDateDesc(1L))
                .thenReturn(transactions);

        List<TransactionDTO> result = transactionService.getAccountTransactions(1L);

        assertEquals(1, result.size());
        assertEquals("Deposit", result.get(0).getTransactionType());
        assertEquals(500.0, result.get(0).getAmount());
        verify(transactionRepository, times(1))
                .findByAccountAccountNoOrderByTransactionDateDesc(1L);
    }

    @Test
    void testGetCustomerTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        when(transactionRepository.findByAccountCustomerIdOrderByTransactionDateDesc(1L))
                .thenReturn(transactions);

        List<TransactionDTO> result = transactionService.getCustomerTransactions(1L);

        assertEquals(1, result.size());
        assertEquals("Deposit", result.get(0).getTransactionType());
        assertEquals(500.0, result.get(0).getAmount());
        verify(transactionRepository, times(1))
                .findByAccountCustomerIdOrderByTransactionDateDesc(1L);
    }
}
