package com.montage.device.repository;

import com.montage.common.repository.BaseRepository;
import com.montage.device.entity.OtaGroupXref;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OtaGroupXrefRepository extends BaseRepository<OtaGroupXref, Integer> {
    
    Page<OtaGroupXref> findByCustomerId(Integer customerId, Pageable pageable);
    
    boolean existsByGroupNameAndCustomerId(String groupName, Integer customerId);
    
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Device d WHERE d.otaGroup.id = :groupId")
    boolean hasAssociatedDevices(@Param("groupId") Integer groupId);
    
    @Query("SELECT o FROM OtaGroupXref o WHERE " +
           "LOWER(o.groupName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "CAST(o.groupId AS string) LIKE CONCAT('%', :searchTerm, '%')")
    Page<OtaGroupXref> searchByTerm(@Param("searchTerm") String searchTerm, Pageable pageable);
} 