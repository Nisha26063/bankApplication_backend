package com.kantartraining.banksystem.Service;

import com.kantartraining.banksystem.dto.AccountDTO;
import com.kantartraining.banksystem.entity.Account;
import com.kantartraining.banksystem.entity.Customer;
import com.kantartraining.banksystem.entity.Transaction;
import com.kantartraining.banksystem.exception.InsufficientBalanceException;
import com.kantartraining.banksystem.exception.ResourceNotFoundException;
import com.kantartraining.banksystem.repository.AccountRepository;
import com.kantartraining.banksystem.repository.CustomerRepository;
import com.kantartraining.banksystem.repository.TransactionRepository;
import com.kantartraining.banksystem.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    private Customer customer;
    private Account account;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("Alice");
        customer.setLastName("Smith");

        account = new Account();
        account.setAccountNo(1L);
        account.setAccountType("Savings");
        account.setCustomer(customer);
        account.setBalance(1000.0);
        account.setTransaction(new ArrayList<>());
    }

    @Test
    void testDeposit() {
        when(accountRepository.findByAccountNo(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(new Transaction());

        AccountDTO result = accountService.deposit(1L, 500.0, LocalDate.now());


        verify(accountRepository, times(1)).save(account);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }



    @Test
    void testGetAccountNotFound() {
        when(accountRepository.findByAccountNo(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () ->
                accountService.getAccount(99L));
    }

    @Test
    void testGetAccountsByCustomerId() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(accountRepository.findByCustomerId(1L)).thenReturn(java.util.List.of(account));

        var result = accountService.getAccountsByCustomerId(1L);

        assertEquals(1, result.size());
        assertEquals("Alice Smith", result.get(0).getAccountHolderName());
    }


}
