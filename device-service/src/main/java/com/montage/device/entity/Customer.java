package com.montage.device.entity;

import com.montage.device.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "customer")
@Audited
@Getter
@Setter
public class Customer extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "Customer name is required")
    @Size(max = 255, message = "Customer name must not exceed 255 characters")
    @Column(name = "customer_name", unique = true)
    private String customerName;
    
    @Size(max = 50, message = "Account number must not exceed 50 characters")
    @Column(name = "account_number", unique = true)
    private String accountNumber;
    
    @Size(max = 100, message = "Short name must not exceed 100 characters")
    @Column(name = "short_name")
    private String shortName;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
} 