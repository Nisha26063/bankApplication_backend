package com.kantartraining.banksystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    @Column(name="transactionType" ,nullable = false)
    private String transactionType;

    @Column(name="amount",nullable = false)
    private double amount;

    @Column(name="transactionDate",nullable = false)
    private LocalDate transactionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_no", nullable = false)
    private Account account;

    @Column(name="balance",nullable = false)
    private double balance;
}