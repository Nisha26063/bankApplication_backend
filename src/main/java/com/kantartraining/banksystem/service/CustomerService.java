package com.kantartraining.banksystem.service;


import com.kantartraining.banksystem.dto.CustomerDTO;
import com.kantartraining.banksystem.entity.Customer;
import com.kantartraining.banksystem.exception.InvalidPasswordException;
import com.kantartraining.banksystem.exception.ResourceNotFoundException;
import com.kantartraining.banksystem.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerDTO createCustomer(CustomerDTO request) {
        log.info("Creating customer with phone: {}", request.getPhone());

        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (customerRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("Phone number already exists");
        }

        Customer customer = new Customer();
                customer.setFirstName(request.getFirstName());
                customer.setLastName(request.getLastName());
                customer.setEmail(request.getEmail());
                customer.setPhone(request.getPhone());
                customer.setPassword(request.getPassword());
                customer.setAddress(request.getAddress());


        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer created successfully with ID: {}", savedCustomer.getId());

        return convertToDTO(savedCustomer);
    }

    public CustomerDTO getCustomer(Long id) {
        log.info("Fetching customer with ID: {}", id);
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return convertToDTO(customer);
    }

    public void changePassword(Long customerId, String currentPassword, String newPassword) {
        log.info("Changing password for customer ID: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if (!customer.getPassword().equals(currentPassword)) {
            throw new InvalidPasswordException("Current password is incorrect");
        }

        customer.setPassword(newPassword);
        customerRepository.save(customer);
        log.info("Password changed successfully for customer ID: {}", customerId);
    }

    public double calculateCibilScore(Long customerId) {
        log.info("Calculating CIBIL score for customer ID: {}", customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));


        double baseScore = 700.0;
        int accountCount = customer.getAccounts().size();
        double score = Math.min(900.0, baseScore + (accountCount * 10) + (Math.random() * 50));

        log.info("CIBIL score calculated: {} for customer ID: {}", score, customerId);
        return score;
    }

    private CustomerDTO convertToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setFirstName(customer.getFirstName());
        dto.setLastName(customer.getLastName());
        dto.setAddress(customer.getAddress());
        dto.setPassword(customer.getPassword());
        dto.setPhone(customer.getPhone());
        dto.setEmail(customer.getEmail());
        List<Long> accountIds = customer.getAccounts()
                .stream()
                .map(account -> account.getAccountNo())
                .collect(Collectors.toList());
        dto.setAccountIds(accountIds);
        return dto;
    }
}