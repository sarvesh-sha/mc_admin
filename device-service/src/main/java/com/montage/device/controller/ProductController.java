package com.montage.device.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.montage.common.dto.ApiResponse;
import com.montage.common.dto.SearchRequest;
import com.montage.device.entity.Product;
import com.montage.device.service.impl.ProductServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Product Management", description = "APIs for managing products")
public class ProductController {

    private final ProductServiceImpl productService;

    @Operation(summary = "Search products", description = "Search products with filtering and pagination")
    @PostMapping("/search")
    public ResponseEntity<ApiResponse<Page<Product>>> search(@RequestBody SearchRequest searchRequest) {
        log.info("Searching products with criteria: {}", searchRequest);
        return ResponseEntity.ok(ApiResponse.success(productService.search(searchRequest)));
    }

    @Operation(summary = "Get product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> findById(@PathVariable Integer id) {
        log.info("Finding product by id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(productService.findById(id)));
    }

    @Operation(summary = "Create new product")
    @PostMapping
    public ResponseEntity<ApiResponse<Product>> create(@Valid @RequestBody Product product) {
        log.info("Creating new product: {}", product);
        return ResponseEntity.ok(ApiResponse.success(productService.create(product)));
    }

    @Operation(summary = "Update existing product")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> update(
            @PathVariable Integer id, 
            @Valid @RequestBody Product product) {
        log.info("Updating product with id {}: {}", id, product);
        return ResponseEntity.ok(ApiResponse.success(productService.update(id, product)));
    }

    @Operation(summary = "Delete product")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        log.info("Deleting product with id: {}", id);
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
} 