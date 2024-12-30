package com.montage.device.repository;

import com.montage.common.repository.BaseRepository;
import com.montage.device.entity.Customer;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends BaseRepository<Customer, Integer> {
    boolean existsByCustomerName(String customerName);
    boolean existsByAccountNumber(String accountNumber);
} 