package com.montage.device.entity;

import com.montage.device.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "sensor")
@Audited
@Getter
@Setter
public class Sensor extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String status;
    
    @Column(name = "mac_address")
    private String macAddress;
    
    @Column(name = "serial_number")
    private Integer serialNumber;
    
    @Column(name = "health_value")
    private Integer healthValue;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    private String location;
    
    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
} 