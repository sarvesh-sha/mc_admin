package com.montage.device.entity;

import com.montage.device.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "ota_group_xref")
@Audited
@Getter
@Setter
public class OtaGroupXref extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    
    @Column(name = "group_id")
    private Integer groupId;
    
    @Column(name = "group_name")
    private String groupName;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
} 