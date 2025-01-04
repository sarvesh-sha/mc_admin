package com.montage.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.montage.device.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>, JpaSpecificationExecutor<Customer> {
    boolean existsByCustomerName(String customerName);
    boolean existsByAccountNumber(String accountNumber);
    
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Device d WHERE d.customer.id = :customerId")
    boolean hasAssociatedDevices(@Param("customerId") Integer customerId);
} 