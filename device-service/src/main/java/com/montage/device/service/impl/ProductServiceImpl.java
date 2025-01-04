package com.montage.device.service.impl;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.montage.common.dto.SearchRequest;
import com.montage.common.specification.GenericSpecificationBuilder;
import com.montage.device.dto.request.ProductRequest;
import com.montage.device.dto.response.ProductResponse;
import com.montage.device.entity.Product;
import com.montage.device.exception.BusinessException;
import com.montage.device.mapper.GenericMapper;
import com.montage.device.repository.ProductRepository;
import com.montage.device.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final GenericMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findById(Integer id) {
        log.debug("Finding product by id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return mapper.convert(product, ProductResponse.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> search(SearchRequest searchRequest) {
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
        
        return productRepository.findAll(specification, pageable)
                .map(product -> mapper.convert(product, ProductResponse.class));
    }

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {
        log.debug("Creating new product: {}", request);
        Product product = mapper.convert(request, Product.class);
        validateProduct(product);
        Product savedProduct = productRepository.save(product);
        return mapper.convert(savedProduct, ProductResponse.class);
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Integer id, ProductRequest request) { 
        log.debug("Updating product with id {}: {}", id, request);
        
        Product existingProduct = findProductById(id);
        Product updatedProduct = mapper.convert(request, Product.class);
        updatedProduct.setId(id);
        
        if (!existingProduct.getSku().equals(updatedProduct.getSku())) {
            validateSku(updatedProduct.getSku());
        }
        
        Product savedProduct = productRepository.save(updatedProduct);
        return mapper.convert(savedProduct, ProductResponse.class);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        log.debug("Deleting product with id: {}", id);
        Product product = findProductById(id);
        
        if (productRepository.hasAssociatedDevices(id)) {
            throw new BusinessException(
                "Cannot delete product with associated devices",
                "PRODUCT_HAS_DEVICES",
                HttpStatus.CONFLICT
            );
        }
        
        productRepository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse findBySku(String sku) {
        log.debug("Finding product by SKU: {}", sku);
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with SKU: " + sku));
        return mapper.convert(product, ProductResponse.class);
    }

    private Product findProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
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
//        if (productRepository.existsByManufacturerAndModel(manufacturer, model)) {
//            throw new BusinessException(
//                "Product with manufacturer '" + manufacturer + "' and model '" + model + "' already exists",
//                "DUPLICATE_MANUFACTURER_MODEL",
//                HttpStatus.CONFLICT
//            );
//        }
    }
} 