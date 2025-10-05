package com.kantartraining.banksystem.repository;


import com.kantartraining.banksystem.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    // Find customer by email
    Optional<Customer> findByEmail(String email);

    // Find customer by phone number
    Optional<Customer> findByPhone(String phone);

    // Check if email exists
    boolean existsByEmail(String email);

    // Check if phone number exists
    boolean existsByPhone(String phone);


}