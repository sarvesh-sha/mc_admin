package com.montage.device.entity.audit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "Audit")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name = "severity_id")
    private Integer severityId;
    
    @Column(name = "category_id")
    private Integer categoryId;
    
    @ManyToOne
    @JoinColumn(name = "event_type_id")
    private EventType eventType;
    
    @Column(name = "update_on")
    private LocalDateTime updateOn;
    
    @Column(name = "details")
    private String details;
    
    @Column(name = "entity_name")
    private String entityName;
    
    @Column(name = "entity_id")
    private Integer entityId;
    
    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;
    
    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;
    
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    
    @Column(name = "created_by")
    private String createdBy;
} 