package com.montage.device.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    
    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    private String uuid = UUID.randomUUID().toString();
    
    @Column(name = "created_on", updatable = false)
    private LocalDateTime createdOn;
    
    @Column(name = "updated_on", insertable = false)
    private LocalDateTime updatedOn;
    
    @Column(name = "created_by", updatable = false)
    private String createdBy;
    
    @Column(name = "updated_by", insertable = false)
    private String updatedBy;
    
    @PrePersist
    protected void onCreate() {
        createdOn = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedOn = LocalDateTime.now();
    }
} 