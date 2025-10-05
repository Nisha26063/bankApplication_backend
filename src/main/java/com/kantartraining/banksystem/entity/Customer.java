package com.kantartraining.banksystem.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Changed from customerId to id

    @Column(name="firstName", length = 50 )
    private String firstName;

    @Column(name="lastName", length = 50 )
    private String lastName;

    @Column(name="address" ,length =200)
    private String address;

    @Column(name="password",length= 150)
    private String password;

    @Column(name="phone",length=10)
    private String phone;

    @Column(name="email",length=150)
    private String email;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Account> accounts = new ArrayList<>();
}