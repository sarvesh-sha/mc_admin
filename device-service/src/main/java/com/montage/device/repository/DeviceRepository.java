package com.montage.device.repository;

import com.montage.device.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer>, JpaSpecificationExecutor<Device> {
    
    // Existing methods
    boolean existsBySerialNumber(Integer serialNumber);
    
    // Add new method for IMEI check
    boolean existsByImei(String imei);
    
    // Optional: Add more useful query methods
    Optional<Device> findByImei(String imei);
    Optional<Device> findBySerialNumber(Integer serialNumber);
    List<Device> findByCustomerId(Integer customerId);
    boolean existsByImeiAndIdNot(String imei, Integer id);
} 