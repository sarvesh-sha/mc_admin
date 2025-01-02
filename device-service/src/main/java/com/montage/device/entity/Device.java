package com.montage.device.entity;

import com.montage.device.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import java.time.LocalDateTime;

@Entity
@Table(name = "device")
@Audited
@Getter
@Setter
public class Device extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "Device type is required")
    @Column(name = "device_type")
    private String deviceType;
    
    @NotBlank(message = "IMEI is required")
    @Column(name = "imei", unique = true)
    private String imei;
    
    @NotBlank(message = "Make is required")
    private String make;
    
    @NotBlank(message = "Model is required")
    private String model;
    
    private String status;
    
    @NotNull(message = "Active status is required")
    @Column(name = "is_active")
    private Boolean isActive;
    
    @Column(name = "installation_date")
    private LocalDateTime installationDate;
    
    @Column(name = "config_version")
    private Integer configVersion;
    
    @Column(name = "firmware_version")
    private Integer firmwareVersion;
    
    @Column(name = "bootloader_version")
    private Integer bootloaderVersion;
    
    private Integer ICCID;
    
    @Column(name = "health_value")
    private Integer healthValue;
    
    @Column(name = "hardware_version")
    private Integer hardwareVersion;
    
    @Column(name = "protocol_version")
    private Integer protocolVersion;
    
    @Column(name = "asset_name")
    private String assetName;
    
    @NotNull(message = "Serial number is required")
    @Column(name = "serial_number")
    private Integer serialNumber;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @NotNull(message = "Customer is required")
    private Customer customer;
    
    @ManyToOne
    @JoinColumn(name = "ota_group_ID")
    private OtaGroupXref otaGroup;
    
    @ManyToOne
    @JoinColumn(name = "provisioning_status_id")
    private ProvisioningStatus provisioningStatus;
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
} 