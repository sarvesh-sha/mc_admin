package com.montage.device.repository;

import com.montage.device.entity.audit.Audit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditRepository extends JpaRepository<Audit, Integer> {
} 