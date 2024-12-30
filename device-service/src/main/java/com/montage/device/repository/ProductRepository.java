package com.montage.device.repository;

import com.montage.common.repository.BaseRepository;
import com.montage.device.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends BaseRepository<Product, Integer> {
    
    Optional<Product> findBySku(String sku);
    
    boolean existsBySku(String sku);
    
    boolean existsByManufacturerAndModel(String manufacturer, String model);
    
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Device d WHERE d.product.id = :productId")
    boolean hasAssociatedDevices(@Param("productId") Integer productId);
    
    @Query("SELECT p FROM Product p WHERE " +
           "LOWER(p.manufacturer) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.model) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(p.sku) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Product> searchByTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
} 