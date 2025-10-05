package com.kantartraining.banksystem.service;

import com.kantartraining.banksystem.dto.AuthRequest;
import com.kantartraining.banksystem.dto.AuthResponse;
import com.kantartraining.banksystem.dto.CustomerDTO;
import com.kantartraining.banksystem.entity.Customer;
import com.kantartraining.banksystem.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepository;

    public AuthResponse register(CustomerDTO customerDTO) {
        // Check if customer already exists
        if (customerRepository.findByEmail(customerDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Customer with this email already exists");
        }

        // Create new customer (store plain text password for now)
        Customer customer = new Customer();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPhone(customerDTO.getPhone());
        customer.setAddress(customerDTO.getAddress());
        customer.setPassword(customerDTO.getPassword()); // Plain text

        Customer savedCustomer = customerRepository.save(customer);

        String token = "token-" + savedCustomer.getId() + "-" + System.currentTimeMillis();

        return new AuthResponse(
                token,
                "Registration successful",
                savedCustomer.getId(),
                savedCustomer.getFirstName(),
                savedCustomer.getLastName(),
                savedCustomer.getEmail()
        );
    }

    public AuthResponse login(AuthRequest authRequest) {
        // Find customer by email
        Customer customer = customerRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Verify password (plain text comparison)
        if (!customer.getPassword().equals(authRequest.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = "token-" + customer.getId() + "-" + System.currentTimeMillis();

        return new AuthResponse(
                token,
                "Login successful",
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getEmail()
        );
    }

    public CustomerDTO getCustomerProfile(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhone(customer.getPhone());
        customerDTO.setAddress(customer.getAddress());

        return customerDTO;
    }
}