package com.montage.device.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.montage.device.entity.audit.Audit;

public interface AuditRepository extends JpaRepository<Audit, Integer>, JpaSpecificationExecutor<Audit> {
    @Query("SELECT a FROM Audit a WHERE a.entityName = :entityName AND a.entityId = :entityId")
    Page<Audit> findByEntityNameAndEntityId(
        @Param("entityName") String entityName,
        @Param("entityId") Integer entityId,
        Pageable pageable
    );
} 