package com.kantartraining.banksystem.Controller;

import com.kantartraining.banksystem.controller.TransactionController;
import com.kantartraining.banksystem.dto.TransactionDTO;
import com.kantartraining.banksystem.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionControllerTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAccountTransactions() {
        TransactionDTO txn = new TransactionDTO();
        txn.setAmount(500.0);

        when(transactionService.getAccountTransactions(1L)).thenReturn(List.of(txn));

        var result = transactionController.getAccountTransactions(1L).getBody();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(500.0, result.get(0).getAmount());
    }

    @Test
    void testGetCustomerTransactions() {
        TransactionDTO txn = new TransactionDTO();
        txn.setAmount(800.0);

        when(transactionService.getCustomerTransactions(1L)).thenReturn(List.of(txn));

        var result = transactionController.getCustomerTransactions(1L).getBody();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(800.0, result.get(0).getAmount());
    }
}
