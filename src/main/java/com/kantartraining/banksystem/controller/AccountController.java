package com.kantartraining.banksystem.controller;


import com.kantartraining.banksystem.dto.AccountDTO;
import com.kantartraining.banksystem.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/customer/{customerId}")
    public ResponseEntity<AccountDTO> createAccount(@PathVariable Long customerId,
                                                    @RequestBody AccountDTO request) {
        AccountDTO response = accountService.createAccount(customerId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}/getAccounts")
    public ResponseEntity<?> getAccountsByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(accountService.getAccountsByCustomerId(customerId));
    }

    @GetMapping("/{accountNo}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long accountNo) {
        AccountDTO response = accountService.getAccount(accountNo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{accountNo}/balance")
    public ResponseEntity<Double> getBalance(@PathVariable Long accountNo) {
        double balance = accountService.getBalance(accountNo);
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/{accountNo}/deposit")
    public ResponseEntity<AccountDTO> deposit(@PathVariable Long accountNo,
                                              @RequestBody Map<String, Object> request) {
        double amount = Double.parseDouble(request.get("amount").toString());
        LocalDate date = LocalDate.parse((String) request.get("date"));
        AccountDTO response = accountService.deposit(accountNo, amount, date);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{accountNo}/withdraw")
    public ResponseEntity<AccountDTO> withdraw(@PathVariable Long accountNo,
                                               @RequestBody Map<String, Object> request) {
        double amount = Double.parseDouble(request.get("amount").toString());
        LocalDate date = LocalDate.parse((String) request.get("date"));

        AccountDTO response = accountService.withdraw(accountNo, amount,date);
        return ResponseEntity.ok(response);
    }
}