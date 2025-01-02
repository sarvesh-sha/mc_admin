package com.montage.device.repository;

import com.montage.device.entity.audit.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventTypeRepository extends JpaRepository<EventType, Integer> {
} 