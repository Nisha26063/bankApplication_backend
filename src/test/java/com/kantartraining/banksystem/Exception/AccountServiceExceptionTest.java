package com.kantartraining.banksystem.Exception;

import com.kantartraining.banksystem.entity.Account;
import com.kantartraining.banksystem.exception.InsufficientBalanceException;
import com.kantartraining.banksystem.exception.ResourceNotFoundException;
import com.kantartraining.banksystem.repository.AccountRepository;
import com.kantartraining.banksystem.repository.CustomerRepository;
import com.kantartraining.banksystem.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceExceptionTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private com.kantartraining.banksystem.service.AccountService accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        account = new Account();
        account.setAccountNo(1L);
        account.setBalance(0.0); // balance 0 for testing insufficient funds
    }


    @Test
    void withdraw_ThrowsResourceNotFoundException() {
        when(accountRepository.findByAccountNo(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                accountService.withdraw(1L, 500.0, LocalDate.now()));
    }

    @Test
    void deposit_ThrowsResourceNotFoundException() {
        when(accountRepository.findByAccountNo(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                accountService.deposit(1L, 500.0, LocalDate.now()));
    }

    @Test
    void deposit_ThrowsRuntimeException_ForNegativeAmount() {
        when(accountRepository.findByAccountNo(1L)).thenReturn(Optional.of(account));

        assertThrows(RuntimeException.class, () ->
                accountService.deposit(1L, -100.0, LocalDate.now()));
    }
}
