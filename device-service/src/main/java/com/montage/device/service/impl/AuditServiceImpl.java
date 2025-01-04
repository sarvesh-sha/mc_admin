package com.montage.device.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.common.utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.montage.device.dto.request.AuditSearchRequest;
import com.montage.device.dto.response.AuditResponse;
import com.montage.device.entity.audit.Audit;
import com.montage.device.entity.audit.EventType;
import com.montage.device.mapper.GenericMapper;
import com.montage.device.repository.AuditRepository;
import com.montage.device.repository.EventTypeRepository;
import com.montage.device.service.AuditService;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;
    private final EventTypeRepository eventTypeRepository;
    private final GenericMapper mapper;

    @Override
    @Transactional
    public void logAuditEvent(String entityName, Integer entityId, String action, String oldValue, String newValue) {
        try {
//            EventType eventType = eventTypeRepository.findByName(action)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid action type: " + action));
//
//            Audit audit = Audit.builder()
//                .entityName(entityName)
//                .entityId(entityId)
//                .eventType(eventType)
//                .oldValue(oldValue)
//                .newValue(newValue)
//                .createdOn(LocalDateTime.now())
//                .createdBy(SecurityUtils.getCurrentUsername())
//                .build();

           // auditRepository.save(audit);
            log.debug("Audit log created for {} with id: {}, action: {}", entityName, entityId, action);
        } catch (Exception e) {
            log.error("Error creating audit log for {} with id: {}, action: {}", entityName, entityId, action, e);
            throw new RuntimeException("Failed to create audit log: " + e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditResponse> searchAudits(AuditSearchRequest request) {
        Specification<Audit> spec = buildSpecification(request);
        
        var pageable = PageRequest.of(
            request.getPage(), 
            request.getSize(), 
            Sort.by(request.getSorts().stream()
                .map(sort -> Sort.Order.by(sort.getField())
                    .with(Sort.Direction.fromString(sort.getDirection())))
                .toList())
        );

        return auditRepository.findAll(spec, pageable)
                .map(audit -> mapper.convert(audit, AuditResponse.class));
    }

    private Specification<Audit> buildSpecification(AuditSearchRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add filters based on search criteria
            if (request.getSeverityId() != null) {
                predicates.add(cb.equal(root.get("severityId"), request.getSeverityId()));
            }

            if (request.getCategoryId() != null) {
                predicates.add(cb.equal(root.get("categoryId"), request.getCategoryId()));
            }

            if (request.getEventTypeId() != null) {
                predicates.add(cb.equal(root.get("eventType").get("id"), request.getEventTypeId()));
            }

            if (StringUtils.hasText(request.getEntityName())) {
                predicates.add(cb.equal(root.get("entityName"), request.getEntityName()));
            }

            if (request.getEntityId() != null) {
                predicates.add(cb.equal(root.get("entityId"), request.getEntityId()));
            }

            if (StringUtils.hasText(request.getCreatedBy())) {
                predicates.add(cb.equal(root.get("createdBy"), request.getCreatedBy()));
            }

            if (request.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdOn"), request.getStartDate()));
            }

            if (request.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdOn"), request.getEndDate()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditResponse> getAuditsByEntity(String entityName, Integer entityId, int page, int size) {
        return auditRepository.findByEntityNameAndEntityId(entityName, entityId, 
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdOn")))
                .map(audit -> mapper.convert(audit, AuditResponse.class));
    }
} 