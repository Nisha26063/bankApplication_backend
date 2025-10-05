package com.kantartraining.banksystem.dto;


import com.kantartraining.banksystem.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private String firstName;
    private String lastName;
    private String address;
    private String password;
    private String phone;
    private String email;
    private List<Long> accountIds;





}
