package com.montage.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.montage.device.entity.ProvisioningStatus;

public interface ProvisioningStatusRepository extends JpaRepository<ProvisioningStatus, Integer>, 
    JpaSpecificationExecutor<ProvisioningStatus> {
    
    boolean existsByName(String name);
    boolean existsByNameIgnoreCase(String name);
} 