package com.montage.device.entity;

import com.montage.device.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "product")
@Audited
@Getter
@Setter
public class Product extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String manufacturer;
    private String model;
    private String make;
    
    @Column(name = "hardware_version")
    private Integer hardwareVersion;
    
    @Column(name = "sku", unique = true)
    private String sku;
} 