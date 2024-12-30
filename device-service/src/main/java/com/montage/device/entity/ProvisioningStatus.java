package com.montage.device.entity;

import com.montage.device.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "provisioning_status")
@Audited
@Getter
@Setter
public class ProvisioningStatus extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @NotBlank(message = "Status name is required")
    @Column(name = "name", unique = true)
    private String name;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
} 