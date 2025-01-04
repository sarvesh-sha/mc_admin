package com.montage.device.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.montage.device.entity.audit.Audit;
import com.montage.device.entity.audit.EventType;
import com.montage.device.enums.EventTypeEnum;
import com.montage.device.repository.AuditRepository;
import com.montage.device.repository.EventTypeRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AuditAspect {
    private final AuditRepository auditRepository;
    private final EventTypeRepository eventTypeRepository;
    private final EntityManager entityManager;
    private final ObjectMapper objectMapper;

    @Around("execution(* com.montage.device.service.impl.*.create(..))")
    public Object auditCreate(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        createAuditEntry(null, result, EventTypeEnum.CREATE);
        return result;
    }

    @Around("execution(* com.montage.device.service.impl.*.update(..))")
    public Object auditUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
        Object oldValue = entityManager.find(joinPoint.getArgs()[1].getClass(), joinPoint.getArgs()[0]);
        Object result = joinPoint.proceed();
        createAuditEntry(oldValue, result, EventTypeEnum.UPDATE);
        return result;
    }

    @Around("execution(* com.montage.device.service.impl.*.delete(..))")
    public Object auditDelete(ProceedingJoinPoint joinPoint) throws Throwable {
    	Integer id = (Integer) joinPoint.getArgs()[0];
        Class<?> entityClass = getEntityClass(joinPoint);  // Helper method to get entity class
        Object oldValue = entityManager.find(entityClass, id);
        
        // Proceed with deletion
        Object result = joinPoint.proceed();
        
        // Create audit entry
        createAuditEntry(oldValue, null, EventTypeEnum.DELETE);
      
        return result;
    }
    private Class<?> getEntityClass(ProceedingJoinPoint joinPoint) {
        // Get the target class name (e.g., DeviceServiceImpl)
        String className = joinPoint.getTarget().getClass().getSimpleName();
        // Remove "ServiceImpl" and get entity name (e.g., Device)
        String entityName = className.replace("ServiceImpl", "");
        try {
            // Construct the full entity class name and return the Class object
            return Class.forName("com.montage.device.entity." + entityName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find entity class for " + entityName, e);
        }
    }

    private void createAuditEntry(Object oldValue, Object newValue, EventTypeEnum eventType) {
        try {
            EventType eventTypeEntity = eventTypeRepository.findById(eventType.getId())
                .orElseThrow(() -> new RuntimeException("Event type not found"));

            Audit audit = Audit.builder()
                .eventType(eventTypeEntity)
                .entityName(newValue != null ? newValue.getClass().getSimpleName() : oldValue.getClass().getSimpleName())
                .entityId(getEntityId(newValue != null ? newValue : oldValue))
                .oldValue(oldValue != null ? objectMapper.writeValueAsString(oldValue) : null)
                .newValue(newValue != null ? objectMapper.writeValueAsString(newValue) : null)
                .createdOn(LocalDateTime.now())
                .createdBy("SYSTEM") // You can get this from security context
                .updateOn(LocalDateTime.now())
                .build();

            auditRepository.save(audit);
        } catch (Exception e) {
            log.error("Error creating audit entry", e);
        }
    }

    private Integer getEntityId(Object entity) {
        try {
            return (Integer) entity.getClass().getMethod("getId").invoke(entity);
        } catch (Exception e) {
            log.error("Error getting entity ID", e);
            return null;
        }
    }
} 