package com.kantartraining.banksystem.Service;

import com.kantartraining.banksystem.dto.CustomerDTO;
import com.kantartraining.banksystem.entity.Account;
import com.kantartraining.banksystem.entity.Customer;
import com.kantartraining.banksystem.exception.InvalidPasswordException;
import com.kantartraining.banksystem.exception.ResourceNotFoundException;
import com.kantartraining.banksystem.repository.CustomerRepository;
import com.kantartraining.banksystem.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john@example.com");
        customer.setPhone("1234567890");
        customer.setPassword("password");
        customer.setAddress("123 Main St");
        customer.setAccounts(new ArrayList<>());
    }

    @Test
    void testCreateCustomerSuccess() {
        when(customerRepository.existsByEmail(customer.getEmail())).thenReturn(false);
        when(customerRepository.existsByPhone(customer.getPhone())).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO dto = new CustomerDTO();
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setEmail("john@example.com");
        dto.setPhone("1234567890");
        dto.setPassword("password");
        dto.setAddress("123 Main St");

        CustomerDTO result = customerService.createCustomer(dto);

        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testCreateCustomerEmailExists() {
        when(customerRepository.existsByEmail(customer.getEmail())).thenReturn(true);
        CustomerDTO dto = new CustomerDTO();
        dto.setEmail(customer.getEmail());
        assertThrows(RuntimeException.class, () -> customerService.createCustomer(dto));
    }

    @Test
    void testGetCustomerSuccess() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        CustomerDTO result = customerService.getCustomer(1L);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void testGetCustomerNotFound() {
        when(customerRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomer(99L));
    }

    @Test
    void testChangePasswordSuccess() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        customerService.changePassword(1L, "password", "newpass");
        assertEquals("newpass", customer.getPassword());
        verify(customerRepository, times(1)).save(customer);
    }

    @Test
    void testChangePasswordInvalidCurrent() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        assertThrows(InvalidPasswordException.class, () ->
                customerService.changePassword(1L, "wrongpass", "newpass"));
    }

    @Test
    void testCalculateCibilScore() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(new Account());
        customer.setAccounts(accounts);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        double score = customerService.calculateCibilScore(1L);
        assertTrue(score >= 700.0 && score <= 900.0);
    }
}
