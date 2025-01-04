package com.montage.device.controller;

import com.montage.common.dto.ApiResponse;
import com.montage.common.dto.SearchRequest;
import com.montage.device.dto.request.CustomerRequest;
import com.montage.device.dto.response.CustomerResponse;
import com.montage.device.service.CustomerService;

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

    private final CustomerService customerService;

    @Operation(summary = "Search customers")
    @PostMapping("v1/customer/search")
    public ResponseEntity<ApiResponse<Page<CustomerResponse>>> searchCustomers(
            @Valid @RequestBody SearchRequest searchRequest) {
        log.info("Searching customers with criteria: {}", searchRequest);
        return ResponseEntity.ok(ApiResponse.success(customerService.search(searchRequest)));
    }

    @Operation(summary = "Get customer by ID")
    @GetMapping("v1/customer/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> getCustomer(
            @PathVariable Integer id) {
        log.info("Finding customer by id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(customerService.findById(id)));
    }

    @Operation(summary = "Create new customer")
    @PostMapping("v1/customer")
    public ResponseEntity<ApiResponse<CustomerResponse>> createCustomer(
            @Valid @RequestBody CustomerRequest request) {
        log.info("Creating new customer: {}", request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(customerService.create(request)));
    }

    @Operation(summary = "Update existing customer")
    @PutMapping("v1/customer/{id}")
    public ResponseEntity<ApiResponse<CustomerResponse>> updateCustomer(
            @PathVariable Integer id, 
            @Valid @RequestBody CustomerRequest request) {
        log.info("Updating customer with id {}: {}", id, request);
        return ResponseEntity.ok(ApiResponse.success(customerService.updateCustomer(id, request)));
    }

    @Operation(summary = "Delete customer")
    @DeleteMapping("v1/customer/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCustomer(
            @PathVariable Integer id) {
        log.info("Deleting customer with id: {}", id);
        customerService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Customer with ID " + id + " successfully deleted"));
    }
} 