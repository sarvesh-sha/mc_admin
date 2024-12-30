package com.montage.device.service.impl;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.montage.common.dto.SearchRequest;
import com.montage.common.service.BaseService;
import com.montage.common.specification.GenericSpecificationBuilder;
import com.montage.device.entity.Product;
import com.montage.device.exception.BusinessException;
import com.montage.device.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements BaseService<Product, Integer> {

    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public Product findById(Integer id) {
        log.debug("Finding product by id: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> search(SearchRequest searchRequest) {
        log.debug("Searching products with criteria: {}", searchRequest);
        
        var specification = new GenericSpecificationBuilder<Product>(searchRequest.getFilters()).build();
        var pageable = PageRequest.of(
            searchRequest.getPage(), 
            searchRequest.getSize(), 
            Sort.by(searchRequest.getSorts().stream()
                .map(sort -> Sort.Order.by(sort.getField())
                    .with(Sort.Direction.fromString(sort.getDirection())))
                .toList())
        );
        
        return productRepository.findAll(specification, pageable);
    }

    @Override
    @Transactional
    public Product create(Product product) {
        log.debug("Creating new product: {}", product);
        validateProduct(product);
        return productRepository.save(product);
    }

    @Override
    @Transactional
    public Product update(Integer id, Product product) {
        log.debug("Updating product with id {}: {}", id, product);
        
        var existingProduct = findById(id);
        
        if (!existingProduct.getSku().equals(product.getSku())) {
            validateSku(product.getSku());
        }
        
        // Update fields
        existingProduct.setManufacturer(product.getManufacturer());
        existingProduct.setModel(product.getModel());
        existingProduct.setSku(product.getSku());
       
        
        return productRepository.save(existingProduct);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.debug("Deleting product with id: {}", id);
        var product = findById(id);
        
        if (productRepository.hasAssociatedDevices(id)) {
            throw new BusinessException(
                "Cannot delete product with associated devices",
                "PRODUCT_HAS_DEVICES",
                HttpStatus.CONFLICT
            );
        }
        
        productRepository.delete(product);
    }

    @Transactional(readOnly = true)
    public Product findBySku(String sku) {
        log.debug("Finding product by SKU: {}", sku);
        return productRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU: " + sku));
    }

    private void validateProduct(Product product) {
        validateSku(product.getSku());
        validateManufacturerAndModel(product.getManufacturer(), product.getModel());
    }

    private void validateSku(String sku) {
        if (productRepository.existsBySku(sku)) {
            throw new BusinessException(
                "Product with SKU '" + sku + "' already exists",
                "DUPLICATE_SKU",
                HttpStatus.CONFLICT
            );
        }
    }

    private void validateManufacturerAndModel(String manufacturer, String model) {
        if (productRepository.existsByManufacturerAndModel(manufacturer, model)) {
            throw new BusinessException(
                "Product with manufacturer '" + manufacturer + "' and model '" + model + "' already exists",
                "DUPLICATE_MANUFACTURER_MODEL",
                HttpStatus.CONFLICT
            );
        }
    }
} 