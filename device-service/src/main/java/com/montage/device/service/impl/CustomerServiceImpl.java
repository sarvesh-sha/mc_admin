package com.montage.device.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.montage.common.dto.SearchRequest;
import com.montage.common.exception.ValidationException;
import com.montage.common.specification.GenericSpecificationBuilder;
import com.montage.device.entity.Customer;
import com.montage.device.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl {

    private final CustomerRepository customerRepository;

    public Customer findById(Integer id) {
        return customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Customer not found with id: %d", id)
            ));
    }

    public Page<Customer> search(SearchRequest searchRequest) {
        try {
            var specification = new GenericSpecificationBuilder<Customer>(searchRequest.getFilters()).build();
            var pageable = PageRequest.of(
                searchRequest.getPage(), 
                searchRequest.getSize(), 
                Sort.by(searchRequest.getSorts().stream()
                    .map(sort -> Sort.Order.by(sort.getField())
                        .with(Sort.Direction.fromString(sort.getDirection())))
                    .toList())
            );
            
            return customerRepository.findAll(specification, pageable);
        } catch (Exception e) {
            throw new ValidationException("Invalid search parameters: " + e.getMessage());
        }
    }

    public Customer create(Customer customer) {
        try {
            List<String> validationErrors = new ArrayList<>();

            // Check if customer with same name already exists
            if (StringUtils.hasText(customer.getCustomerName()) && 
                customerRepository.existsByCustomerName(customer.getCustomerName())) {
                validationErrors.add(String.format("Customer with name '%s' already exists", 
                    customer.getCustomerName()));
                throw new ValidationException(validationErrors);
            }

            // Check if account number is already in use
            if (StringUtils.hasText(customer.getAccountNumber()) && 
                customerRepository.existsByAccountNumber(customer.getAccountNumber())) {
                validationErrors.add(String.format("Customer with account number '%s' already exists", 
                    customer.getAccountNumber()));
                throw new ValidationException(validationErrors);
            }

            if (customer.getIsActive() == null) {
                customer.setIsActive(true);
            }

            log.info("Creating new customer with name: {}", customer.getCustomerName());
            return customerRepository.save(customer);
        } catch (Exception e) {
            if (e instanceof ValidationException) {
                throw e;
            }
            List<String> errors = new ArrayList<>();
            errors.add("Error creating customer: " + e.getMessage());
            throw new ValidationException(errors);
        }
    }

    public Customer update(Integer id, Customer customer) {
        List<String> validationErrors = validateCustomerData(customer);
        
        Customer existingCustomer = findById(id);

        // Additional update-specific validations
        if (StringUtils.hasText(customer.getCustomerName()) &&
            !customer.getCustomerName().equals(existingCustomer.getCustomerName()) &&
            customerRepository.existsByCustomerName(customer.getCustomerName())) {
            validationErrors.add(String.format("Customer with name '%s' already exists", customer.getCustomerName()));
        }

        if (StringUtils.hasText(customer.getAccountNumber()) &&
            !customer.getAccountNumber().equals(existingCustomer.getAccountNumber()) &&
            customerRepository.existsByAccountNumber(customer.getAccountNumber())) {
            validationErrors.add(String.format("Customer with account number '%s' already exists", customer.getAccountNumber()));
        }

        if (!validationErrors.isEmpty()) {
            throw new ValidationException(validationErrors);
        }

        existingCustomer.setCustomerName(customer.getCustomerName());
        existingCustomer.setShortName(customer.getShortName());
        existingCustomer.setAccountNumber(customer.getAccountNumber());
        existingCustomer.setIsActive(customer.getIsActive());

        log.info("Updating customer with id: {}", id);
        return customerRepository.save(existingCustomer);
    }

    public void delete(Integer id) {
        try {
            Customer customer = findById(id);
            customerRepository.delete(customer);
        } catch (Exception e) {
            if (e instanceof ResourceNotFoundException) {
                throw e;
            }
            throw new ValidationException("Error deleting customer: " + e.getMessage());
        }
    }

    private List<String> validateCustomerData(Customer customer) {
        List<String> errors = new ArrayList<>();

        if (customer == null) {
            errors.add("Customer data cannot be null");
            return errors;
        }

        if (!StringUtils.hasText(customer.getCustomerName())) {
            errors.add("Customer name cannot be empty");
        }
        
        if (customer.getCustomerName() != null && customer.getCustomerName().length() > 255) {
            errors.add("Customer name is too long (maximum 255 characters)");
        }

        if (StringUtils.hasText(customer.getAccountNumber()) && customer.getAccountNumber().length() > 50) {
            errors.add("Account number is too long (maximum 50 characters)");
        }

        if (StringUtils.hasText(customer.getShortName()) && customer.getShortName().length() > 100) {
            errors.add("Short name is too long (maximum 100 characters)");
        }

        // Check if customer with same name already exists
        if (StringUtils.hasText(customer.getCustomerName()) && 
            customerRepository.existsByCustomerName(customer.getCustomerName())) {
            errors.add(String.format("Customer with name '%s' already exists", customer.getCustomerName()));
        }

        // Check if account number is already in use
        if (StringUtils.hasText(customer.getAccountNumber()) && 
            customerRepository.existsByAccountNumber(customer.getAccountNumber())) {
            errors.add(String.format("Customer with account number '%s' already exists", customer.getAccountNumber()));
        }

        return errors;
    }
} 