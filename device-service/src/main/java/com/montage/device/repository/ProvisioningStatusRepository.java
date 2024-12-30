package com.montage.device.repository;

import com.montage.common.repository.BaseRepository;
import com.montage.device.entity.ProvisioningStatus;
import org.springframework.stereotype.Repository;

@Repository
public interface ProvisioningStatusRepository extends BaseRepository<ProvisioningStatus, Integer> {
    boolean existsByNameIgnoreCase(String name);
} 