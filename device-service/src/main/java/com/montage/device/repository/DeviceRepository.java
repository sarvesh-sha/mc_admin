package com.montage.device.repository;

import com.montage.common.repository.BaseRepository;
import com.montage.device.entity.Device;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends BaseRepository<Device, Integer> {
} 