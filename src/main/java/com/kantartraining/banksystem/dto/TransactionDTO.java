package com.kantartraining.banksystem.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private String transactionType;
    private double amount;
    private LocalDate transactionDate;
    private Long accountId;
    private double balance;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthRequest {
        private String email;
        private String password;
    }

    public static class AuthResponse {
    }
}
