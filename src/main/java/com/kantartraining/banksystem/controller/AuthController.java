package com.kantartraining.banksystem.controller;

import com.kantartraining.banksystem.dto.AuthRequest;
import com.kantartraining.banksystem.dto.AuthResponse;
import com.kantartraining.banksystem.dto.CustomerDTO;
import com.kantartraining.banksystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") // Allow Angular app to access
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody CustomerDTO customerDTO) {
        try {
            AuthResponse response = authService.register(customerDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new AuthResponse(null, "Registration failed: " + e.getMessage(), null, null, null, null)
            );
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            AuthResponse response = authService.login(authRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new AuthResponse(null, "Login failed: " + e.getMessage(), null, null, null, null)
            );
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<CustomerDTO> getCustomerProfile(@PathVariable Long customerId) {
        try {
            CustomerDTO customerDTO = authService.getCustomerProfile(customerId);
            return ResponseEntity.ok(customerDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
