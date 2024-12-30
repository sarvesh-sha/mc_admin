package com.montage.device.repository;

import com.montage.common.repository.BaseRepository;
import com.montage.device.entity.Sensor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SensorRepository extends BaseRepository<Sensor, Integer> {
    
    Page<Sensor> findByDeviceId(Integer deviceId, Pageable pageable);
    
    boolean existsByMacAddressIgnoreCase(String macAddress);
    
    @Query("SELECT s FROM Sensor s WHERE s.device.customer.id = :customerId")
    Page<Sensor> findByCustomerId(@Param("customerId") Integer customerId, Pageable pageable);
    
    @Query("SELECT s FROM Sensor s WHERE " +
           "LOWER(s.macAddress) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(s.location) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "CAST(s.serialNumber AS string) LIKE CONCAT('%', :searchTerm, '%')")
    Page<Sensor> searchByTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT COUNT(s) FROM Sensor s WHERE s.device.id = :deviceId AND s.isActive = true")
    long countActiveByDeviceId(@Param("deviceId") Integer deviceId);
} 