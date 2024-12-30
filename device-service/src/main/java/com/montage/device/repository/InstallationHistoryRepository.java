package com.montage.device.repository;

import com.montage.common.repository.BaseRepository;
import com.montage.device.entity.InstallationHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface InstallationHistoryRepository extends BaseRepository<InstallationHistory, Integer> {
    
    Page<InstallationHistory> findByDeviceId(Integer deviceId, Pageable pageable);
    
    Page<InstallationHistory> findByCustomerId(Integer customerId, Pageable pageable);
    
    @Query("SELECT ih FROM InstallationHistory ih WHERE " +
           "LOWER(ih.status) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(ih.installedBy) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(ih.installationCode) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<InstallationHistory> searchByTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
    
    @Query("SELECT ih FROM InstallationHistory ih WHERE " +
           "ih.device.id = :deviceId ORDER BY ih.installationFinishDate DESC")
    Page<InstallationHistory> findLatestByDeviceId(@Param("deviceId") Integer deviceId, Pageable pageable);
} 