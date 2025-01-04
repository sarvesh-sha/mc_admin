package com.montage.device.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.montage.device.entity.audit.EventType;

public interface EventTypeRepository extends JpaRepository<EventType, Integer> {
    
    @Query("SELECT et FROM EventType et WHERE LOWER(et.name) = LOWER(:name)")
    Optional<EventType> findByName(@Param("name") String name);
    
    boolean existsByName(String name);
    
   
} 