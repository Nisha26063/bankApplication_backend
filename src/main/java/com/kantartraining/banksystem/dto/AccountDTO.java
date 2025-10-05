package com.kantartraining.banksystem.dto;


import com.kantartraining.banksystem.entity.Customer;
import com.kantartraining.banksystem.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AccountDTO {

    private Long accountNo;
    private String accountType;
    private String accountHolderName;
    private Long customerId;
    private Double balance;
    private List<Long> transactionIds;

}
