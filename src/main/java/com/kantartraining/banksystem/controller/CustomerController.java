package com.kantartraining.banksystem.controller;

import com.kantartraining.banksystem.dto.CustomerDTO;
import com.kantartraining.banksystem.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO request) {
        CustomerDTO response = customerService.createCustomer(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
        CustomerDTO response = customerService.getCustomer(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable Long id,
                                               @RequestBody Map<String, String> request) {
        customerService.changePassword(id, request.get("currentPassword"), request.get("newPassword"));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/cibil-score")
    public ResponseEntity<Double> getCibilScore(@PathVariable Long id) {
        double score = customerService.calculateCibilScore(id);
        return ResponseEntity.ok(score);
    }
}