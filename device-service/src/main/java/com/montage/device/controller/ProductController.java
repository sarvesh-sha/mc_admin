package com.montage.device.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.montage.common.dto.ApiResponse;
import com.montage.common.dto.SearchRequest;
import com.montage.device.dto.request.ProductRequest;
import com.montage.device.dto.response.ProductResponse;
import com.montage.device.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products Management", description = "APIs for managing products")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "Search products", description = "Search products with filtering and pagination")
    @PostMapping("v1/product/search")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> searchProducts(
            @RequestBody SearchRequest searchRequest) {
        log.info("Searching products with criteria: {}", searchRequest);
        return ResponseEntity.ok(ApiResponse.success(productService.search(searchRequest)));
    }

    @Operation(summary = "Get product by ID")
    @GetMapping("v1/product/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProduct(
            @PathVariable Integer id) {
        log.info("Finding product by id: {}", id);
        return ResponseEntity.ok(ApiResponse.success(productService.findById(id)));
    }

    @Operation(summary = "Create new product")
    @PostMapping("v1/product")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest request) {
        log.info("Creating new product: {}", request);
        return ResponseEntity.ok(ApiResponse.success(productService.create(request)));
    }

    @Operation(summary = "Update existing product")
    @PutMapping("v1/product/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Integer id, 
            @Valid @RequestBody ProductRequest request) {
        log.info("Updating product with id {}: {}", id, request);
        return ResponseEntity.ok(ApiResponse.success(productService.updateProduct(id, request)));
    }

    @Operation(summary = "Delete product")
    @DeleteMapping("v1/product/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(
            @PathVariable Integer id) {
        log.info("Deleting product with id: {}", id);
        productService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Product with ID " + id + " successfully deleted"));
    }
} 