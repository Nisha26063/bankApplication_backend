package com.kantartraining.banksystem.Controller;

import com.kantartraining.banksystem.controller.CustomerController;
import com.kantartraining.banksystem.dto.CustomerDTO;
import com.kantartraining.banksystem.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCustomer() {
        CustomerDTO request = new CustomerDTO();
        request.setFirstName("Alice");
        request.setEmail("alice@example.com");

        CustomerDTO response = new CustomerDTO();
        response.setFirstName("Alice");
        response.setEmail("alice@example.com");

        when(customerService.createCustomer(any(CustomerDTO.class))).thenReturn(response);

        CustomerDTO result = customerController.createCustomer(request).getBody();

        assertNotNull(result);
        assertEquals("Alice", result.getFirstName());
        assertEquals("alice@example.com", result.getEmail());
    }

    @Test
    void testChangePassword() {
        Map<String, String> request = new HashMap<>();
        request.put("currentPassword", "oldpass");
        request.put("newPassword", "newpass");

        doNothing().when(customerService).changePassword(eq(1L), eq("oldpass"), eq("newpass"));

        assertDoesNotThrow(() -> customerController.changePassword(1L, request));
    }

    @Test
    void testGetCibilScore() {
        when(customerService.calculateCibilScore(1L)).thenReturn(750.0);

        Double score = customerController.getCibilScore(1L).getBody();

        assertNotNull(score);
        assertEquals(750.0, score);
    }
}
