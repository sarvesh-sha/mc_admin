package com.montage.device.entity.audit;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "event_type")
@Getter
@Setter
public class EventType {
    @Id
    private Integer id;
    
    @Column(name = "name")
    private String name;
} 