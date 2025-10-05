package com.kantartraining.banksystem.controller;


import com.kantartraining.banksystem.dto.TransactionDTO;
import com.kantartraining.banksystem.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/account/{accountNo}")
    public ResponseEntity<List<TransactionDTO>> getAccountTransactions(@PathVariable Long accountNo) {
        List<TransactionDTO> responses = transactionService.getAccountTransactions(accountNo);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<TransactionDTO>> getCustomerTransactions(@PathVariable Long customerId) {
        List<TransactionDTO> responses = transactionService.getCustomerTransactions(customerId);
        return ResponseEntity.ok(responses);
    }
}