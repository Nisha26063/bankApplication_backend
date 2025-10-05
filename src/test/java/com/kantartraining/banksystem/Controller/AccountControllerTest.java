package com.kantartraining.banksystem.Controller;

import com.kantartraining.banksystem.controller.AccountController;
import com.kantartraining.banksystem.dto.AccountDTO;
import com.kantartraining.banksystem.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount() {
        AccountDTO request = new AccountDTO();
        request.setAccountHolderName("John Doe");
        request.setAccountType("Savings");
        request.setCustomerId(1L);

        AccountDTO response = new AccountDTO();
        response.setAccountHolderName("John Doe");
        response.setAccountType("Savings");
        response.setCustomerId(1L);

        when(accountService.createAccount(eq(1L), any(AccountDTO.class))).thenReturn(response);

        AccountDTO result = accountController.createAccount(1L, request).getBody();

        assertNotNull(result);
        assertEquals("John Doe", result.getAccountHolderName());
        assertEquals("Savings", result.getAccountType());
    }

    @Test
    void testDeposit() {
        Map<String, Object> request = new HashMap<>();
        request.put("amount", 1000.0);
        request.put("date", LocalDate.now().toString());

        AccountDTO response = new AccountDTO();


        when(accountService.deposit(eq(1L), anyDouble(), any(LocalDate.class))).thenReturn(response);

        AccountDTO result = accountController.deposit(1L, request).getBody();

        assertNotNull(result);

    }

    @Test
    void testWithdraw() {
        Map<String, Object> request = new HashMap<>();
        request.put("amount", 500.0);
        request.put("date", LocalDate.now().toString());

        AccountDTO response = new AccountDTO();


        when(accountService.withdraw(eq(1L), anyDouble(), any(LocalDate.class))).thenReturn(response);

        AccountDTO result = accountController.withdraw(1L, request).getBody();

        assertNotNull(result);

    }

    @Test
    void testGetBalance() {
        when(accountService.getBalance(1L)).thenReturn(1500.0);

        Double balance = accountController.getBalance(1L).getBody();

        assertNotNull(balance);
        assertEquals(1500.0, balance);
    }
}
