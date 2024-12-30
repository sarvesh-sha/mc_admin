package com.montage.device.controller;

import com.montage.common.dto.ApiResponse;
import com.montage.common.dto.SearchRequest;
import com.montage.device.entity.Customer;
import com.montage.device.service.impl.CustomerServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerServiceImpl customerService;

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Page<Customer>>> search(@Valid @RequestBody SearchRequest searchRequest) {
        log.info("Searching customers with criteria: {}", searchRequest);
        return ResponseEntity.ok(ApiResponse.success(customerService.search(searchRequest)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Customer>> findById(@PathVariable Integer id) {
        log.info("Finding customer by id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(customerService.findById(id)));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<Customer>> create(@Valid @RequestBody Customer customer) {
        log.info("Creating new customer: {}", customer);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(customerService.create(customer)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Customer>> update(
            @PathVariable Integer id, 
            @Valid @RequestBody Customer customer) {
        log.info("Updating customer with id {}: {}", id, customer);
        return ResponseEntity.ok(ApiResponse.success(customerService.update(id, customer)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("Deleting customer with id: {}", id);
        customerService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 