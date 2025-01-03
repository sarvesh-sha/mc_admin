package com.montage.device.controller;

import com.montage.common.dto.ApiResponse;
import com.montage.common.dto.SearchRequest;
import com.montage.device.entity.Customer;
import com.montage.device.service.impl.CustomerServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Management", description = "APIs for managing customers")
public class CustomerController {

    private final CustomerServiceImpl customerService;

    @Operation(summary = "Search customers")
    @PostMapping("v1/customer/search")
    public ResponseEntity<ApiResponse<Page<Customer>>> searchCustomers(
            @Valid @RequestBody SearchRequest searchRequest) {
        log.info("Searching customers with criteria: {}", searchRequest);
        return ResponseEntity.ok(ApiResponse.success(customerService.search(searchRequest)));
    }

    @Operation(summary = "Get customer by ID")
    @GetMapping("v1/customer/{id}")
    public ResponseEntity<ApiResponse<Customer>> getCustomer(
            @PathVariable Integer id) {
        log.info("Finding customer by id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(customerService.findById(id)));
    }

    @Operation(summary = "Create new customer")
    @PostMapping("v1/customer")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<Customer>> createCustomer(
            @Valid @RequestBody Customer customer) {
        log.info("Creating new customer: {}", customer);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(customerService.create(customer)));
    }

    @Operation(summary = "Update existing customer")
    @PutMapping("v1/customer/{id}")
    public ResponseEntity<ApiResponse<Customer>> updateCustomer(
            @PathVariable Integer id, 
            @Valid @RequestBody Customer customer) {
        log.info("Updating customer with id {}: {}", id, customer);
        return ResponseEntity.ok(ApiResponse.success(customerService.update(id, customer)));
    }

    @Operation(summary = "Delete customer")
    @DeleteMapping("v1/customer/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable Integer id) {
        log.info("Deleting customer with id: {}", id);
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 