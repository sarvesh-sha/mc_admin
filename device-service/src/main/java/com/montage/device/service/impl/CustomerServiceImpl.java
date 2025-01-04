package com.montage.device.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.montage.common.dto.SearchRequest;
import com.montage.common.exception.BusinessException;
import com.montage.common.exception.ValidationException;
import com.montage.common.specification.GenericSpecificationBuilder;
import com.montage.device.dto.request.CustomerRequest;
import com.montage.device.dto.response.CustomerResponse;
import com.montage.device.entity.Customer;
import com.montage.device.mapper.GenericMapper;
import com.montage.device.repository.CustomerRepository;
import com.montage.device.service.CustomerService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final GenericMapper mapper;

    @Override
    public CustomerResponse findById(Integer id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Customer not found with id: %d", id)
            ));
        return mapper.convert(customer, CustomerResponse.class);
    }

    @Override
    public Page<CustomerResponse> search(SearchRequest searchRequest) {
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
            
            return customerRepository.findAll(specification, pageable)
                    .map(customer -> mapper.convert(customer, CustomerResponse.class));
        } catch (Exception e) {
            throw new ValidationException("Invalid search parameters: " + e.getMessage());
        }
    }

    @Override
    public CustomerResponse create(CustomerRequest request) {
        try {
            Customer customer = mapper.convert(request, Customer.class);
            validateNewCustomer(customer);
            
            log.info("Creating new customer with name: {}", customer.getCustomerName());
            Customer savedCustomer = customerRepository.save(customer);
            return mapper.convert(savedCustomer, CustomerResponse.class);
            
        } catch (Exception e) {
            if (e instanceof ValidationException) {
                throw e;
            }
            throw new ValidationException("Error creating customer: " + e.getMessage());
        }
    }

    @Override
    public CustomerResponse updateCustomer(Integer id, CustomerRequest request) {
        Customer existingCustomer = findCustomerById(id);
        Customer updatedCustomer = mapper.convert(request, Customer.class);
        updatedCustomer.setId(id);
        
        validateCustomerUpdate(updatedCustomer, existingCustomer);

//        existingCustomer.setCustomerName(updatedCustomer.getCustomerName());
//        existingCustomer.setShortName(updatedCustomer.getShortName());
//        existingCustomer.setAccountNumber(updatedCustomer.getAccountNumber());
//        existingCustomer.setIsActive(updatedCustomer.getIsActive());

        log.info("Updating customer with id: {}", id);
        Customer savedCustomer = customerRepository.save(updatedCustomer);
        return mapper.convert(savedCustomer, CustomerResponse.class);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        try {
            Customer customer = findCustomerById(id);
            
            if (customerRepository.hasAssociatedDevices(id)) {
                throw new BusinessException(
                    "Cannot delete customer with associated devices",
                    "CUSTOMER_HAS_DEVICES",
                    HttpStatus.CONFLICT
                );
            }
            
            customerRepository.delete(customer);
        } catch (Exception e) {
            if (e instanceof ResourceNotFoundException || e instanceof BusinessException) {
                throw e;
            }
            throw new BusinessException(
                "Error deleting customer: " + e.getMessage(),
                "DELETE_CUSTOMER_ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    private Customer findCustomerById(Integer id) {
        return customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                String.format("Customer not found with id: %d", id)
            ));
    }

    private void validateNewCustomer(Customer customer) {
        List<String> errors = new ArrayList<>();

        if (!StringUtils.hasText(customer.getCustomerName())) {
            errors.add("Customer name cannot be empty");
        }

        if (customerRepository.existsByCustomerName(customer.getCustomerName())) {
            errors.add(String.format("Customer with name '%s' already exists", customer.getCustomerName()));
        }

        if (StringUtils.hasText(customer.getAccountNumber()) && 
            customerRepository.existsByAccountNumber(customer.getAccountNumber())) {
            errors.add(String.format("Customer with account number '%s' already exists", customer.getAccountNumber()));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

    private void validateCustomerUpdate(Customer updatedCustomer, Customer existingCustomer) {
        List<String> errors = new ArrayList<>();

        if (StringUtils.hasText(updatedCustomer.getCustomerName()) &&
            !updatedCustomer.getCustomerName().equals(existingCustomer.getCustomerName()) &&
            customerRepository.existsByCustomerName(updatedCustomer.getCustomerName())) {
            errors.add(String.format("Customer with name '%s' already exists", updatedCustomer.getCustomerName()));
        }

        if (StringUtils.hasText(updatedCustomer.getAccountNumber()) &&
            !updatedCustomer.getAccountNumber().equals(existingCustomer.getAccountNumber()) &&
            customerRepository.existsByAccountNumber(updatedCustomer.getAccountNumber())) {
            errors.add(String.format("Customer with account number '%s' already exists", updatedCustomer.getAccountNumber()));
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }
} 